package de.inovex.fbuerkle.thesis_prototype;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class NumberInputFragment extends Fragment {

	private int i;

	public NumberInputFragment() {
		// Required empty public constructor
		i = 0;
	}

	public NumberInputFragment(int i){
		this.i = i;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View layout;
		switch (this.i){
			case 0:
				layout = inflater.inflate(R.layout.number_input_1,null);
				break;
			case 1:
				layout = inflater.inflate(R.layout.number_input_2,null);
				break;
			default:
				layout = inflater.inflate(R.layout.number_input_1,null);
				break;
		}
		return layout;
	}


}
