package internat.adapter.chaternat;

import internat.chaternat.R;
import internat.db.chaternat.Answer;
import internat.db.chaternat.Question;
import internat.models.SerieModel;
import android.content.Context;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


public class SerieListViewAdapter extends BaseExpandableListAdapter {

	private Context context;
	private SerieModel serie;
	private LayoutInflater inflater;
	
	public SerieListViewAdapter(Context context, SerieModel serie) {
		// TODO Auto-generated constructor stub
		this.setContext(context);
		this.serie = serie;
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public boolean areAllItemsEnabled(){
		return true;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return serie.getAnswer(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Answer answer = (Answer) getChild(groupPosition, childPosition);
		
		ChildViewHolder childViewHolder;
		
		if (convertView == null){
			childViewHolder = new ChildViewHolder();
			
			convertView = inflater.inflate(R.layout.serie_child, null);
			
			childViewHolder.textViewChild = (TextView) convertView.findViewById(R.id.txt_child);
			childViewHolder.chkBoxChild = (CheckBox) convertView.findViewById(R.id.chk_child);
			
			convertView.setTag(childViewHolder);
		}else{
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
		
		childViewHolder.textViewChild.setText(answer.getText());
		childViewHolder.textViewChild.setMovementMethod(new ScrollingMovementMethod());
		childViewHolder.chkBoxChild.setText(answer.getLetter() + ") ");
		
		childViewHolder.chkBoxChild.setChecked(serie.getChosenAnswer(groupPosition).contains(answer.getLetter()));
		
		if(serie.getAnswer(groupPosition).get(childPosition).getSolution()){
			childViewHolder.textViewChild.setTextColor(context.getResources().getColor(R.color.success));
			childViewHolder.chkBoxChild.setTextColor(context.getResources().getColor(R.color.success));
		}
		else{
			childViewHolder.textViewChild.setTextColor(context.getResources().getColor(R.color.fail));
			childViewHolder.chkBoxChild.setTextColor(context.getResources().getColor(R.color.fail));
		}
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return serie.getAnswer(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return serie.getQuestionsList().get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return serie.getQuestionsList().size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		GroupViewHolder groupViewHolder;
		
		Question question = (Question) getGroup(groupPosition);
		
		if (convertView == null){
			groupViewHolder = new GroupViewHolder();
			
			convertView = inflater.inflate(R.layout.serie_group, null);
			
			groupViewHolder.textViewGroup = (TextView) convertView.findViewById(R.id.txt_group);
			
			convertView.setTag(groupViewHolder);
		}else
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		
		groupViewHolder.textViewGroup.setText(question.getQuestionNumber() + "." + question.getText());
		
		if (serie.getResultsForQuestion(groupPosition))
			groupViewHolder.textViewGroup.setBackgroundColor(Color.GREEN);
		else
			groupViewHolder.textViewGroup.setBackgroundColor(Color.RED);
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	class GroupViewHolder {
		public TextView textViewGroup;
	}

	class ChildViewHolder {
		public TextView textViewChild;
		public CheckBox chkBoxChild;
	}

}
