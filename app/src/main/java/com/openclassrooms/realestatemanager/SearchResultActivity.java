package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.adapters.PropertyAdapter;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.MediaTemp;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyDisplayAllInfo;
import com.openclassrooms.realestatemanager.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.viewmodels.ViewModelFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultActivity extends BaseActivity implements PropertyAdapter.OnDispatchListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    String query;
    List<Object> params;

    private PropertyViewModel mPropertyViewModel;
    private List<PropertyDisplayAllInfo> mPropertyList;
    private PropertyAdapter mPropertyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        showReturnHome();

        query = getIntent().getStringExtra(SearchActivity.QUERY_EXTRA);
        params = Arrays.asList((Object [])getIntent().getSerializableExtra(SearchActivity.PARAMS_EXTRA));

        configureViewModels();

        configureAdapter();

        doSearch();
    }

    private void configureViewModels() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);

        this.mPropertyViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PropertyViewModel.class);
    }

    private void configureAdapter(){
        mPropertyList = new ArrayList<>();

        mPropertyAdapter = new PropertyAdapter(this, mPropertyList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mPropertyAdapter);

    }


    private void doSearch(){
        mPropertyViewModel.searchProperties(query, params)
                .observe(this, this::fetchResults);
    }

    private void fetchResults(List<PropertyDisplayAllInfo> propertyList){
        for (PropertyDisplayAllInfo padi : propertyList) {
            Property property = padi.getProperty();
            assert property != null;

            mPropertyViewModel.getSelectedMedia(property.getId())
                    .observe(this, media -> {
                        if(media != null) {
                            MediaTemp mediaTemp = new MediaTemp();
                            mediaTemp.setId(media.getId());
                            mediaTemp.setCover(media.isCover());
                            mediaTemp.setLabel(media.getLabel());
                            mediaTemp.setFileName(media.getFileName());

                            padi.setMediaTemp(mediaTemp);
                        }
                    });

            mPropertyViewModel.getPropertyType(property.getPropertyTypeId())
                    .observe(this, propertyType ->
                    {
                        padi.setPropertyType(propertyType);
//                        propertyTypeLoaded = true;
//                        reloadData();

                        mPropertyViewModel.getPropertyAddress(property.getId())
                                .observe(this, addressDisplayedInfo ->
                                {
                                    if(addressDisplayedInfo != null) {
                                        Iterator<Address> it = addressDisplayedInfo.getAddress().iterator();
                                        Address address = it.next();
                                        padi.setAddress(address);

//                                        addressLoaded = true;
                                        reloadRecyclerViewData();
                                    }
                                });
                    });

        }

        mPropertyList = propertyList;
    }

    public void reloadRecyclerViewData() {
        mPropertyAdapter.setPropertyList(mPropertyList);
    }

    @Override
    public void onItemClick(Property property) {
        showDetailsInActivity(property.getId());
    }

    private void showDetailsInActivity(long propertyId) {
        Intent intent = new Intent(this, PropertyDetailsActivity.class);
        intent.putExtra(PropertyDetailsActivity.EXTRA_PROPERTY_ID, propertyId);
        startActivity(intent);
    }
}
