package com.urja.motoservice.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.urja.motoservice.R;
import com.urja.motoservice.database.ServiceRequest;
import com.urja.motoservice.fragment.TransactionDetailActivityFragment.OnListFragmentInteractionListener;
import com.urja.motoservice.fragment.dummy.DummyContent.DummyItem;
import com.urja.motoservice.model.Transaction;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyOrderForServicesTransactionRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = MyOrderForServicesTransactionRecyclerViewAdapter.class.getSimpleName();
    private final List<Transaction> mValues;
    private final OnListFragmentInteractionListener mListener;
    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
    private static final String MSG_TOTAL_AMOUNT = "Total Amount: Rs. ";

    public MyOrderForServicesTransactionRecyclerViewAdapter(List<Transaction> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TransactionViewHolder transactionViewHolder = null;

        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        switch (viewType){
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
                viewHolder = new EmptyTransactionViewHolder(view);
                break;
            case VIEW_TYPE_OBJECT_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_orderforservicestransaction, parent, false);// fragment_item
                viewHolder = new TransactionViewHolder(view);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case VIEW_TYPE_OBJECT_VIEW:
                TransactionViewHolder transactionViewHolder = (TransactionViewHolder) holder;
                Transaction transaction = mValues.get(position);
                transactionViewHolder.mItem = transaction;
                List<ServiceRequest> requests = transaction.getServiceRequestList();
                int totalAmount = 0;
                for (ServiceRequest request : requests){
                    int val = Integer.valueOf(request.getVehiclegroup());
                    totalAmount +=val;
                }
                //holder.mIdView.setText(mValues.get(position).id);
                //holder.mContentView.setText(mValues.get(position).content);
                String carNumber = transaction.getServiceRequestList().get(0).getCarnumber();
                transactionViewHolder.mCarNumber.setText(carNumber);
                transactionViewHolder.mTransactionID.setText(transaction.getTransactionId());

                transactionViewHolder.mTransactionDate.setText(transaction.getServiceRequestDate());
                transactionViewHolder.mTransactionStatus.setText(transaction.getRequestStatus().toUpperCase());
                transactionViewHolder.mTotalAmount.setText(MSG_TOTAL_AMOUNT+totalAmount);
                break;
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                Log.e(TAG, "onBindViewHolder: EMPTY TEXT VIEW" );
                EmptyTransactionViewHolder emptyTransactionViewHolder = (EmptyTransactionViewHolder) holder;
                emptyTransactionViewHolder.mEmptyText.setText("No Data Available!");
                break;
        }


        /*holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mValues.isEmpty()) {
            return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;
        } else {
            return VIEW_TYPE_OBJECT_VIEW;
        }
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCarNumber;
        public final TextView mTransactionID;
        public final TextView mTransactionDate;
        public final TextView mTransactionStatus;
        public final TextView mTotalAmount;
        public Transaction mItem;

        public TransactionViewHolder(View view) {
            super(view);
            mView = view;
            mCarNumber = (TextView) view.findViewById(R.id.carNumber);
            mTransactionID = (TextView) view.findViewById(R.id.transactionID);
            mTransactionDate = (TextView) view.findViewById(R.id.transactionDate);
            mTransactionStatus = (TextView) view.findViewById(R.id.transactionStatus);
            mTotalAmount = (TextView) view.findViewById(R.id.totalAmount);
        }
    }

    public class EmptyTransactionViewHolder extends RecyclerView.ViewHolder {

        private final TextView mEmptyText;
        public EmptyTransactionViewHolder(View itemView) {
            super(itemView);
            mEmptyText = (TextView) itemView.findViewById(R.id.no_data);
        }
    }
}
