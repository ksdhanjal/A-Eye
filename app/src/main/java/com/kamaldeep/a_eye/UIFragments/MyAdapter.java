package com.kamaldeep.a_eye.UIFragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kamaldeep.a_eye.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    private final String TAG = MyAdapter.class.getSimpleName();

    private List<String> stuRollNo;
    private List<String> stuName;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public TextView txtSNo;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            txtSNo = v.findViewById(R.id.serial_number);
        }
    }

    public void add(int position, String item) {
        stuRollNo.add(position, item);
        notifyItemInserted(position);
    }

//    public void remove(int position) {
//        stuRollNo.remove(position);
//        notifyItemRemoved(position);
//    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<String> myDataset , List<String> name) {
        stuRollNo = myDataset;
        stuName = name;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String rno = stuRollNo.get(position);
        final String name = stuName.get(position);
        int display_pos = position+1;
        String pos = ""+display_pos;
        Log.e(TAG,pos);
        holder.txtHeader.setText(rno);
        holder.txtFooter.setText(name);
        holder.txtSNo.setText(pos);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return stuRollNo.size();
    }

}
