package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import de.inovex.fbuerkle.datamodel.Checklist;


public class MainActivity extends Activity{

	private static final String TAG = "de.inovex.fbuerkle.checklist";

	public Checklist currentChecklist;

	public MainActivity(){
		super();
		this.currentChecklist = new Select()
				.from(Checklist.class)
				.where("Name = ?","Test")
				.executeSingle();

		if(currentChecklist == null){
			currentChecklist = new Checklist();
			currentChecklist.name = "Test";
			currentChecklist.save();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(	R.layout.activity_main);

		ListView listView = (ListView) findViewById(R.id.listView);
		ChecklistAdapter adapter = new ChecklistAdapter(this, currentChecklist);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(adapter);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id){
			case R.id.action_add_item:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Pick Item-Type")
						.setItems(R.array.item_types,new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int position) {
								switch (position){
									case 0:
										NewItemFragment cf = new NewItemFragment(ItemTypes.Check);
										cf.show(getFragmentManager(),"NewCheckItemFragment");
										break;
									case 1:
										NewItemFragment df = new NewItemFragment(ItemTypes.Decision);
										df.show(getFragmentManager(), "NewDescisionItemFragment");
										break;
									default:
										Toast.makeText(MainActivity.this,"Not Implemented",Toast.LENGTH_SHORT).show();
								}
							}
						});
				builder.show();
				return true;
			case R.id.action_sync:
				// TODO sync checklists
				return true;
			case R.id.action_add_checklist:
				new NewChecklistFragment().show(getFragmentManager(),"newChecklistFragment");
				return true;
			case R.id.action_settings:
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
