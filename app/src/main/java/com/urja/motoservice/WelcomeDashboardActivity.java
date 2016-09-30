package com.urja.motoservice;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.urja.motoservice.adapters.VehicleTypeAdapter;
import com.urja.motoservice.database.CarServicePrice;
import com.urja.motoservice.database.DbHelper;
import com.urja.motoservice.database.dao.CarServicePriceDao;
import com.urja.motoservice.database.dao.ServiceRequestDao;
import com.urja.motoservice.fragment.TransactionListActivityFragment;
import com.urja.motoservice.model.CarCareDetailing;
import com.urja.motoservice.model.Customer;
import com.urja.motoservice.model.Size;
import com.urja.motoservice.model.Vehicle;
import com.urja.motoservice.utils.AlertDialog;
import com.urja.motoservice.utils.AppConstants;
import com.urja.motoservice.utils.CurrentLoggedInUser;
import com.urja.motoservice.utils.FirebaseRootReference;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WelcomeDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TransactionListActivityFragment.OnListFragmentInteractionListener {

    private TextView mPersonName;
    private TextView mPersonEmail;
    private String mEmail;

    private static final String TAG = WelcomeDashboardActivity.class.getSimpleName();
    private FirebaseAuth mAuth = null;
    private static final int PROFILE_SETTING = 100000;
    private Toolbar mToolbar;
    private final int IDENTIFIER_SIGNOUT = 101;
    private ProgressBar mProgressBar;
    private DatabaseReference mVehicleTypesRef;
    private DatabaseReference mCustomerDatabaseRef;
    private DatabaseReference transactionDataRef;
    private DatabaseReference mCarCareDetailingDatabaseRef;

    private static String mName = "";
    private static String mMobile;
    private static String mCurrentUserId;

    private RecyclerView recyclerView;
    private VehicleTypeAdapter adapter;
    private List<Vehicle> mVehicleList;
    private Vehicle mVehicle;

    private List<CarServicePrice> carServicePriceList;


    private CarServicePriceDao carServicePriceDao ;

    private ImageView mPersonImage;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_welcome_dashboard);

        Intent intent = getIntent();
        if (intent != null){//Assuming this intent comes from CustomerDetailActivityFragment
            String transactionId = intent.getStringExtra(AppConstants.TRANSACTIOIN_ID);
            if (transactionId != null && transactionId == "")
                AlertDialog.Alert(WelcomeDashboardActivity.this, "We Received your Request!!", "Your Transaction Id : "+ transactionId);
        }

        mVehicleTypesRef = FirebaseRootReference.getInstance().getmVehicleTypesRef();

        //Get the Currentlogged in UserId
        if (CurrentLoggedInUser.getCurrentFirebaseUser() == null)
            CurrentLoggedInUser.setCurrentFirebaseUser(mAuth.getCurrentUser());

        mCurrentUserId = CurrentLoggedInUser.getCurrentFirebaseUser().getUid();
        mName = CurrentLoggedInUser.getName();
        mEmail = CurrentLoggedInUser.getCurrentFirebaseUser().getEmail();


        //Initialize toolbar
        initCollapsingToolbar();

        //Navigation Drawer
        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        initializeDrawer(savedInstanceState);

        //Initialize Grid layout for Vehicles
        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.vehicle_recycler_view);
        mVehicleList = new ArrayList<>();
        adapter = new VehicleTypeAdapter(this, mVehicleList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //add vehicle list to adapter
        fetchAndShowVehicles();

        //Listen Value Event Listener for Customer
        listenCustomerChangeEvent(mCurrentUserId);

        //initialize carprice table
        carServicePriceDao = DbHelper.getInstance(WelcomeDashboardActivity.this).getCarServicePriceDao();
        if (carServicePriceDao.loadAll().size()<1){
            carServicePriceList = new ArrayList<>();
            populateCarServicePriceTable(carServicePriceDao);
        }

    }

    /**
     * Inserts data into the Carservice table from the Firebase database for one time read
     * @param carServicePriceDao
     */
    private void populateCarServicePriceTable(final CarServicePriceDao carServicePriceDao) {
        Log.e(TAG, "populateCarServicePriceTable: " );
        mCarCareDetailingDatabaseRef = FirebaseRootReference.get_instance().getmCarCareDetailingRef();
        mCarCareDetailingDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            CarServicePrice carServicePrice = null;
            CarCareDetailing carCareDetailing = null;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    carCareDetailing = new CarCareDetailing();
                    carCareDetailing.setCode(childSnapshot.getKey());
                    for (DataSnapshot snapshot : childSnapshot.getChildren()){
                        String key = snapshot.getKey();
                        if(key.equalsIgnoreCase(AppConstants.ValidVehicle.VALID_VEHICLE_DESC)){
                            carCareDetailing.setDesc(snapshot.getValue(String.class));
                        }else if (key.equalsIgnoreCase(AppConstants.ValidVehicle.VALID_VEHICLE_SIZE)){
                            Size size = snapshot.getValue(Size.class);
                            carCareDetailing.setSize(size);
                        }
                    }
                    //Insert into CarService table
                    carServicePrice = new CarServicePrice();
                    carServicePrice.setServiceCode(carCareDetailing.getCode());
                    carServicePrice.setServiceDesc(carCareDetailing.getDesc());
                    carServicePrice.setPriceSmall(carCareDetailing.getSize().getS().toString());
                    carServicePrice.setPriceMedium(carCareDetailing.getSize().getM().toString());
                    carServicePrice.setPriceLarge(carCareDetailing.getSize().getL().toString());
                    carServicePriceList.add(carServicePrice);
                }
                carServicePriceDao.insertOrReplaceInTx(carServicePriceList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void listenCustomerChangeEvent(String mCurrentUserId) {
        mCustomerDatabaseRef = FirebaseRootReference.get_instance().getmCustomerDatabaseRef();
        mCustomerDatabaseRef.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Customer customer = dataSnapshot.getValue(Customer.class);
                String newName = customer.getName();
                TextDrawable textDrawable = TextDrawable.builder().buildRound(newName.substring(0, 1).toUpperCase(), ColorGenerator.MATERIAL.getRandomColor());
                mPersonImage.setBackground(textDrawable);
                mPersonImage.setImageDrawable(textDrawable);
                mPersonName.setText(newName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchAndShowVehicles() {
        mVehicleTypesRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mVehicle = snapshot.getValue(Vehicle.class);
                    mVehicle.setCarType(snapshot.getKey());
                    mVehicleList.add(mVehicle);
                }
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void initializeDrawer(Bundle savedInstanceState) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        MenuItem draftTranMenu =  menu.findItem(R.id.nav_draft_transaction);

        /*ServiceRequestDao serviceRequestDao = DbHelper.getInstance(WelcomeDashboardActivity.this).getServiceRequestDao();
        if (serviceRequestDao.count() < 1)
            draftTranMenu.setVisible(false);*/

        View hView = navigationView.getHeaderView(0);
        mPersonName = (TextView) hView.findViewById(R.id.person_name);
        mPersonEmail = (TextView) hView.findViewById(R.id.person_email);
        mPersonImage = (ImageView) hView.findViewById(R.id.person_name_img);

        Log.d(TAG, "initializeDrawer() called with: " + "mName = [" + mName + "]");

        TextDrawable textDrawable = TextDrawable.builder().buildRound(mName.substring(0, 1).toUpperCase(), ColorGenerator.MATERIAL.getRandomColor());
        mPersonImage.setBackground(textDrawable);
        mPersonImage.setImageDrawable(textDrawable);

        mPersonName.setText(mName);
        mPersonEmail.setText(mEmail);
    }

    private void updateHeaderData(String mName, String mEmail) {
        mPersonName.setText(mName);
        mPersonEmail.setText(mName);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        Fragment fragment = null;

        if (id == R.id.nav_transaction) {
                intent = new Intent(WelcomeDashboardActivity.this, TransactionListActivity.class);
        } else if (id == R.id.nav_draft_transaction) {//Shows previously saved transaction
            ServiceRequestDao requestDao = DbHelper.getInstance(WelcomeDashboardActivity.this).getServiceRequestDao();
            if (requestDao.count() > 0)
                intent = new Intent(WelcomeDashboardActivity.this, ModifyChoosenServiceRquestActivity.class);
        } else if (id == R.id.nav_manage_profile) {
            intent = new Intent(WelcomeDashboardActivity.this, UpdateProfileActivity.class);
        } else if (id == R.id.nav_signout) {
            mAuth.signOut();
            intent = new Intent(WelcomeDashboardActivity.this, LoginActivity.class);
        }

        //Logout the user and take him back to the LoginActivity
        if (intent != null && id == R.id.nav_signout) {
            WelcomeDashboardActivity.this.startActivity(intent);
            finish();
        } else if (intent != null) {
            WelcomeDashboardActivity.this.startActivity(intent);
        }else if (intent == null){
            Toast.makeText(WelcomeDashboardActivity.this, "No Saved Transactions Available.", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onListFragmentInteraction(String transactionId) {
        //Do Nothing
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
