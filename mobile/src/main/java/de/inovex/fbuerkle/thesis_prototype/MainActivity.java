package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

import de.inovex.fbuerkle.DataKeys;
import de.inovex.fbuerkle.Paths;
import de.inovex.fbuerkle.datamodel.Checklist;
import de.inovex.fbuerkle.datamodel.Questions.ChecklistItem;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

	private static final String TAG = "MainActivity";

	public Checklist currentChecklist;
	private ListView mDrawerList;
	private ListView checkListView;
	private GoogleApiClient mGoogleApiClient;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	public MainActivity() {
		super();
		this.currentChecklist = new Select()
				.from(Checklist.class)
				.executeSingle();

		if (currentChecklist == null) {
			currentChecklist = new Checklist();
			currentChecklist.name = "Test";
			currentChecklist.save();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		checkListView = (ListView) findViewById(R.id.listView);
		ChecklistAdapter adapter = new ChecklistAdapter(this, currentChecklist);
		checkListView.setAdapter(adapter);
		checkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setMessage(MainActivity.this.getString(R.string.do_you_want_to_delete_this_item))
						.setTitle(MainActivity.this.getString(R.string.confirm_delete))
						.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								ChecklistItem item = (ChecklistItem) parent.getAdapter().getItem(position);
								item.delete();
								((BaseAdapter)MainActivity.this.checkListView.getAdapter()).notifyDataSetChanged();
								// TODO delete DataItem
							}
						})
						.setNegativeButton("Cancel", null);
				builder.show();
			}
		});

		View view = findViewById(R.id.add_button);
		if(view != null){
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					newItem();
				}
			});
		}

		this.mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
				.build();
		mGoogleApiClient.connect();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setAdapter(new ChecklistSelectAdapter(this));
		mDrawerList.setOnItemClickListener(this);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.string.drawer_open,
				R.string.drawer_close
		){
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				//getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				((ChecklistSelectAdapter)mDrawerList.getAdapter()).notifyDataSetChanged();
				//getActionBar().setTitle();
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.setDrawerIndicatorEnabled(true);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		int id = item.getItemId();

		switch (id) {
			case R.id.action_add_item:
				newItem();
				return true;
			case R.id.action_sync:
				// TODO sync CHECKLISTS
				publishEverything();
				return true;
			case R.id.action_add_checklist:
				new NewChecklistFragment().show(getFragmentManager(), "newChecklistFragment");
				return true;
			case R.id.action_settings:
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void newItem() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick Item-Type")
				.setItems(R.array.item_types, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int position) {
						switch (position) {
							case 0:
								NewItemFragment cf = new NewItemFragment(ItemTypes.Check);
								cf.show(getFragmentManager(), "NewCheckItemFragment");
								break;
							case 1:
								NewItemFragment df = new NewItemFragment(ItemTypes.Decision);
								df.show(getFragmentManager(), "NewDescisionItemFragment");
								break;
							default:
								Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
						}
					}
				});
		this.publishChecklist(currentChecklist);
		builder.show();
	}

	private void publishEverything() {
		publishListOfChecklists();
		for(Checklist list : Checklist.getAll()){
			publishChecklist(list);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		currentChecklist = (Checklist) mDrawerList.getAdapter().getItem(position);
		checkListView.setAdapter(new ChecklistAdapter(this,currentChecklist));
		mDrawerList.setItemChecked(position,true);
		((DrawerLayout)findViewById(R.id.drawer_layout)).closeDrawer(mDrawerList);
		getActionBar().setTitle(currentChecklist.name);
	}


	public void publishListOfChecklists(){
		List<Checklist> checklists =  new Select().from(Checklist.class).execute();
		ArrayList<String> listNames = new ArrayList<String>();
		for(Checklist list : checklists){
			if(list.items().size()>0){
				listNames.add(list.name);
			}
		}
		PutDataMapRequest dataMap = PutDataMapRequest.create(Paths.PREFIX + Paths.CHECKLISTS);
		dataMap.getDataMap().putStringArrayList(DataKeys.CHECKLISTS,listNames);
		PutDataRequest request = dataMap.asPutDataRequest();
		PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient,request);
		pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
			@Override
			public void onResult(DataApi.DataItemResult dataItemResult) {
				Log.d(TAG, dataItemResult.getStatus().toString() + " " + dataItemResult.getDataItem().getUri());
			}
		});
	}

	public void publishChecklist(Checklist checklist){
		List<ChecklistItem> checklistItems = checklist.items();
		PutDataMapRequest checklistMap = PutDataMapRequest.create(Paths.PREFIX + Paths.CHECKLIST + checklist.name.toLowerCase().replace(" ",""));
		checklist.putToDataMap(checklistMap.getDataMap());
		Wearable.DataApi.putDataItem(mGoogleApiClient, checklistMap.asPutDataRequest());
		for(int i = 0; i < checklistItems.size(); i++){
			PutDataMapRequest itemMap = PutDataMapRequest
					.create(Paths.PREFIX + Paths.CHECKLIST + checklist.name.toLowerCase().replace(" ","") + Paths.ITEMS + i);
			checklistItems.get(i).putToDataMap(itemMap.getDataMap());
			Wearable.DataApi.putDataItem(mGoogleApiClient, itemMap.asPutDataRequest())
					.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
						@Override
						public void onResult(DataApi.DataItemResult dataItemResult) {
							Log.d(TAG, dataItemResult.getStatus().toString() + " " + dataItemResult.getDataItem().getUri());
						}
					});
		}
	}
}
