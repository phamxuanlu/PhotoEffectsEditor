package com.ss.photoeffectseditor.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ss.photoeffectseditor.AppConfigs;
import com.ss.photoeffectseditor.R;
import com.ss.photoeffectseditor.adapters.EditorListToolsAdapter;
import com.ss.photoeffectseditor.models.ToolObject;
import com.ss.photoeffectseditor.utils.GLog;

import java.util.List;

import it.sephiroth.android.library.widget.HListView;

/**
 * Created by phamxuanlu@gmail.com on 3/6/2015.
 */
public class EditorToolsFragment extends Fragment {

    private View mFragmentView;
    private HListView featuresList;
    private List<ToolObject> lstTools;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GLog.v("EditorToolsFragment", "" + AppConfigs.getInstance().editorFeatures.size());
        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.fragment_ip_features, null);
            featuresList = (HListView) mFragmentView.findViewById(R.id.listTransformation);
            EditorListToolsAdapter adapter = new EditorListToolsAdapter(getActivity(), AppConfigs.getInstance().editorFeatures);
            featuresList.setAdapter(adapter);
        } else {
            ((ViewGroup) mFragmentView.getParent()).removeView(mFragmentView);
        }
        return mFragmentView;
    }


//    private AdapterView.OnItemClickListener clk = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            EditorActivity editor = (EditorActivity) getActivity();
//            if (!obj.transform.equals(editor.getTransformation())) { //Tránh thực hiện 2 lần. Tuy nhiên sau khi apply thì có thể thực hiện thêm lần nữa
//                editor.doTransform(obj.transform);
//            }
//        }
//    } ;


}
