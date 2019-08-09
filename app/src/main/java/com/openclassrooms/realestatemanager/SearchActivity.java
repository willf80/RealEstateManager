package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.SearchListItem;
import com.openclassrooms.realestatemanager.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.viewmodels.ViewModelFactory;
import com.openclassrooms.realestatemanager.views.SearchDateQueryBuilderView;
import com.openclassrooms.realestatemanager.views.SearchListQueryBuilderView;
import com.openclassrooms.realestatemanager.views.SearchQueryBuilderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {
    public static final String QUERY_EXTRA = "query";
    public static final String PARAMS_EXTRA = "params";

    @BindView(R.id.nearbyPointsSearchListQueryBuilderView)
    SearchListQueryBuilderView nearbyPointsSearchListQueryBuilderView;

    @BindView(R.id.typeOfPropertySearchListQueryBuilderView)
    SearchListQueryBuilderView typeOfPropertySearchListQueryBuilderView;

    @BindView(R.id.priceSearchQueryBuilderView)
    SearchQueryBuilderView priceSearchQueryBuilderView;

    @BindView(R.id.entryOfMarketSearchDateQueryBuilderView)
    SearchDateQueryBuilderView entryOfMarketSearchDateQueryBuilderView;

    @BindView(R.id.soldSearchDateQueryBuilderView)
    SearchDateQueryBuilderView soldSearchDateQueryBuilderView;

    @BindView(R.id.mediaSearchQueryBuilderView)
    SearchQueryBuilderView mediaSearchQueryBuilderView;

    @BindView(R.id.addressLine1EditText)
    TextInputEditText addressLine1EditText;

    @BindView(R.id.postalCodeEditText)
    TextInputEditText postalCodeEditText;

    private PropertyViewModel mPropertyViewModel;

    List<Object> queryParams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        showReturnHome();

        configureViewModels();

        List<SearchListItem> nearbyPointsSearchListItems = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            SearchListItem searchListItem = new SearchListItem();
            searchListItem.setId(i);
            searchListItem.setTitle("Item " + i);

            nearbyPointsSearchListItems.add(searchListItem);
        }
        nearbyPointsSearchListQueryBuilderView.setSearchList(nearbyPointsSearchListItems);

        List<SearchListItem> typeOfPropertySearchListItems = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SearchListItem searchListItem = new SearchListItem();
            searchListItem.setId(i);
            searchListItem.setTitle("Item " + i);

            typeOfPropertySearchListItems.add(searchListItem);
        }
        typeOfPropertySearchListQueryBuilderView.setSearchList(typeOfPropertySearchListItems);
    }

    private void configureViewModels() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);

        this.mPropertyViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PropertyViewModel.class);
    }

    private boolean isSearchAddressUsed() {
        String addressLine = addressLine1EditText.getText().toString();
        String postalCode = postalCodeEditText.getText().toString();

        return !addressLine.isEmpty() || !postalCode.isEmpty();
    }

    @OnClick(R.id.searchButton)
    public void onSearchClick() {
        String query = buildSearchQuery();

        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(QUERY_EXTRA, query);
        intent.putExtra(PARAMS_EXTRA, queryParams.toArray());

        startActivity(intent);
    }

    private String buildSearchQuery() {
        //Get all same tables view elements

        queryParams.clear();

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT _p.* FROM Property as _p");

        //JOIN AddressProperties
        if(isSearchAddressUsed()){
            builder.append(" INNER JOIN ");
            builder.append("AddressProperties");
            builder.append(" as _addressProperties");
            builder.append(" ON _p.id = _addressProperties.propertyId");

            //JOIN Address
            builder.append(" INNER JOIN ");
            builder.append("Address");
            builder.append(" as _address");
            builder.append(" ON _addressProperties.addressId = _address.id");
        }

        //JOIN PropertyInterestPoints
        if(nearbyPointsSearchListQueryBuilderView.isUsed()){
            builder.append(" INNER JOIN ");
            builder.append("PropertyInterestPoints");
            builder.append(" as _propertyInterestPoints");
            builder.append(" ON _p.id = _propertyInterestPoints.propertyId");
        }

        //JOIN Media
        if(mediaSearchQueryBuilderView.isUsed()){
            builder.append(" INNER JOIN ");
            builder.append("Media");
            builder.append(" as _media");
            builder.append(" ON _p.id = _media.propertyId");
        }


        ///----------------------------------

        builder.append(" WHERE ");

        boolean canAddLink = false;

        if(isSearchAddressUsed()) {
            String addressLine = addressLine1EditText.getText().toString();
            String postalCode = postalCodeEditText.getText().toString();

            StringBuilder addressQueryBuilder = new StringBuilder();

            addressQueryBuilder.append(" ( ");
            if(!addressLine.isEmpty()){
                addressQueryBuilder.append("_address.addressLine1 LIKE ?");
                queryParams.add("%" + addressLine + "%");
            }

            addressQueryBuilder.append(" [OR] ");

            if(!postalCode.isEmpty()){
                addressQueryBuilder.append("_address.postalCode = ? ");
                queryParams.add(postalCode);
            }
            addressQueryBuilder.append(" ) ");

            String addressQuery = addressQueryBuilder.toString();
            if(!addressLine.isEmpty() && !postalCode.isEmpty()){
                addressQuery = addressQuery.replace("[OR]", "OR");
            }else{
                addressQuery = addressQuery.replace("[OR]", "");
            }

            builder.append(addressQuery);
            canAddLink = true;
        }

        if(typeOfPropertySearchListQueryBuilderView.isUsed()){
            if(canAddLink){
                addAndLink(builder);
            }

            builder.append(" ( ");
            builder.append("_p.propertyTypeId ");
            builder.append(typeOfPropertySearchListQueryBuilderView.getSign());
            if(" IN ".equalsIgnoreCase(
                    typeOfPropertySearchListQueryBuilderView.getSign())){

                List<Integer> ids = (List<Integer>) typeOfPropertySearchListQueryBuilderView.getData();
                builder.append("(");
                for (int i = 0; i < ids.size(); i++) {
                    builder.append("'");
                    builder.append(ids.get(i));
                    builder.append("'");

                    if(i + 1 < ids.size()){
                        builder.append(",");
                    }
                }
                builder.append(")");

            }else{
                builder.append(" ? ");
                queryParams.add(typeOfPropertySearchListQueryBuilderView.getData());
            }
            builder.append(" ) ");


            canAddLink = true;
        }

        if(priceSearchQueryBuilderView.isUsed()){
            if(canAddLink){
                addAndLink(builder);
            }

            builder.append(" ( ");
            builder.append("_p.price ");
            builder.append(priceSearchQueryBuilderView.getSign());

            SearchQueryBuilderView.QueryData queryData = priceSearchQueryBuilderView.getData();

            if(" BETWEEN ".equalsIgnoreCase(priceSearchQueryBuilderView.getSign())){
                builder.append(" ? AND ? ");
                queryParams.add(queryData.getMinValue());
                queryParams.add(queryData.getMaxValue());
            }else{
                builder.append(" ? ");
                queryParams.add(queryData.getMinValue());
            }
            builder.append(" ) ");

            canAddLink = true;
        }


        if(nearbyPointsSearchListQueryBuilderView.isUsed()){
            if(canAddLink){
                addAndLink(builder);
            }

            builder.append(" ( ");
            builder.append("_propertyInterestPoints.interestId ");
            builder.append(nearbyPointsSearchListQueryBuilderView.getSign());
            if(" IN ".equalsIgnoreCase(
                    nearbyPointsSearchListQueryBuilderView.getSign())){

                List<Integer> ids = (List<Integer>) nearbyPointsSearchListQueryBuilderView.getData();
                builder.append("(");
                for (int i = 0; i < ids.size(); i++) {
                    builder.append("'");
                    builder.append(ids.get(i));
                    builder.append("'");

                    if(i + 1 < ids.size()){
                        builder.append(",");
                    }
                }
                builder.append(")");

            }else{
                builder.append(" ? ");
                queryParams.add(nearbyPointsSearchListQueryBuilderView.getData());
            }
            builder.append(" ) ");

            canAddLink = true;
        }


        if(entryOfMarketSearchDateQueryBuilderView.isUsed()){
            if(canAddLink){
                addAndLink(builder);
            }

            builder.append(" ( ");
            builder.append("_p.createdDate ");
            builder.append(entryOfMarketSearchDateQueryBuilderView.getSign());

            SearchDateQueryBuilderView.QueryData queryData =
                    entryOfMarketSearchDateQueryBuilderView.getData();

            if(" BETWEEN ".equalsIgnoreCase(entryOfMarketSearchDateQueryBuilderView.getSign())){
                builder.append(" ? AND ? ");
                queryParams.add(queryData.getStartDate());
                queryParams.add(queryData.getEndDate());
            }else{
                builder.append(" ? ");
                queryParams.add(queryData.getStartDate());
            }
            builder.append(" ) ");

            canAddLink = true;
        }

        if(soldSearchDateQueryBuilderView.isUsed()){
            if(canAddLink){
                addAndLink(builder);
            }

            builder.append(" ( ");
            builder.append("_p.soldDate ");
            builder.append(soldSearchDateQueryBuilderView.getSign());

            SearchDateQueryBuilderView.QueryData queryData =
                    soldSearchDateQueryBuilderView.getData();

            if(" BETWEEN ".equalsIgnoreCase(soldSearchDateQueryBuilderView.getSign())){
                builder.append(" ? AND ? ");
                queryParams.add(queryData.getStartDate());
                queryParams.add(queryData.getEndDate());
            }else{
                builder.append(" ? ");
                queryParams.add(queryData.getStartDate());
            }
            builder.append(" ) ");

            canAddLink = true;
        }

        boolean canReplaceWhere = false;
        if(mediaSearchQueryBuilderView.isUsed()){
            if(canAddLink){
                addGroupBy(builder, "_p.id, _media.id");
                builder.append(" HAVING ");
            }else{
                canReplaceWhere = true;
            }

            builder.append(" ( ");
            builder.append("COUNT(_media.propertyId) ");
            builder.append(mediaSearchQueryBuilderView.getSign());

            SearchQueryBuilderView.QueryData queryData =
                    mediaSearchQueryBuilderView.getData();

            if(" BETWEEN ".equalsIgnoreCase(mediaSearchQueryBuilderView.getSign())){
                builder.append(" ? AND ? ");
                queryParams.add(queryData.getMinValue());
                queryParams.add(queryData.getMaxValue());
            }else{
                builder.append(" ? ");
                queryParams.add(queryData.getMinValue());
            }
            builder.append(" ) ");
        }

        String s = builder.toString();
        if(canReplaceWhere){

            StringBuilder sb = new StringBuilder();
            addGroupBy(sb, "_p.id, _media.propertyId");
            s = s.replace("WHERE",
                    sb.toString() + " HAVING");
        }

        return s;
    }

    private void addGroupBy(StringBuilder builder, String criteria){
        builder.append(" GROUP BY ");
        builder.append(criteria);
    }

    private void addAndLink(StringBuilder builder){
        builder.append(" AND ");
    }
}
