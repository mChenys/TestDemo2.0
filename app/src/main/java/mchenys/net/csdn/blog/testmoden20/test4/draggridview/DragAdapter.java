package mchenys.net.csdn.blog.testmoden20.test4.draggridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import mchenys.net.csdn.blog.testmoden20.R;
import mchenys.net.csdn.blog.testmoden20.test4.framework.OnItemDragListener;
public class DragAdapter extends BaseAdapter implements OnItemDragListener {
	private List<HashMap<String, Object>> list;
	private LayoutInflater mInflater;
	private int mHidePosition = -1;
	
	public DragAdapter(Context context, List<HashMap<String, Object>> list){
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 由于复用convertView导致某些item消失了，所以这里不复用item，
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.grid_item, null);
		ImageView mImageView = (ImageView) convertView.findViewById(R.id.item_image);
		TextView mTextView = (TextView) convertView.findViewById(R.id.item_text);
		if (position == getCount() - 1) {
			mImageView.setImageResource(R.drawable.ic_add);
			mTextView.setVisibility(View.GONE);
		} else {
			mImageView.setImageResource((Integer) list.get(position).get("item_image"));
			mTextView.setText((CharSequence) list.get(position).get("item_text"));
			mTextView.setVisibility(View.VISIBLE);
		}

		
		if(position == mHidePosition){
			convertView.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}
	

	@Override
	public void onChange(int from, int to) {
		HashMap<String, Object> temp = list.get(from);
		if(from < to){
			for(int i = from; i< to; i++){
				Collections.swap(list, i, i+1);
			}
		}else if(from > to){
			for(int i = from; i> to; i--){
				Collections.swap(list, i, i-1);
			}
		}
		
		list.set(to, temp);
	}

	@Override
	public void setItemHideAndNotify(int hidePosition) {
		this.mHidePosition = hidePosition; 
		notifyDataSetChanged();
	}


}
