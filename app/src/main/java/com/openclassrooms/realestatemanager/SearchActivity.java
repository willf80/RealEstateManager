package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.InterestPoint;
import com.openclassrooms.realestatemanager.models.PropertyType;
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

    @BindView(R.id.surfaceSearchQueryBuilderView)
    SearchQueryBuilderView surfaceSearchQueryBuilderView;

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
    List<SearchListItem> interestPointsSearchListItems = new ArrayList<>();
    List<SearchListItem> typeOfPropertySearchListItems = new ArrayList<>();
    private boolean canSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        showReturnHome();

        configureViewModels();

        nearbyPointsSearchListQueryBuilderView.setSearchList(interestPointsSearchListItems);
        typeOfPropertySearchListQueryBuilderView.setSearchList(typeOfPropertySearchListItems);

        loadInterestPointsList();
        loadTypeOfPropertyList();
    }

    private void configureViewModels() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);

        this.mPropertyViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(PropertyViewModel.class);
    }

    private void loadTypeOfPropertyList(){
        mPropertyViewModel.getPropertyTypes()
                .observe(this, this::fetchTypeOfPropertyList);
    }

    private void loadInterestPointsList(){
        mPropertyViewModel.getInterestPoints()
                .observe(this, this::fetchInterestPointsList);
    }

    private void fetchTypeOfPropertyList(List<PropertyType> propertyTypeList){
        typeOfPropertySearchListItems.clear();
        for (PropertyType propertyType : propertyTypeList) {
            SearchListItem searchListItem = new SearchListItem();
            searchListItem.setId(propertyType.getId());
            searchListItem.setTitle(propertyType.getLabel());
            typeOfPropertySearchListItems.add(searchListItem);
        }

        typeOfPropertySearchListQueryBuilderView.setSearchList(typeOfPropertySearchListItems);
    }

    private void fetchInterestPointsList(List<InterestPoint> interestPointList){
        interestPointsSearchListItems.clear();
        for (InterestPoint interestPoint : interestPointList) {
            SearchListItem searchListItem = new SearchListItem();
            searchListItem.setId(interestPoint.getId());
            searchListItem.setTitle(interestPoint.getLabel());

            interestPointsSearchListItems.add(searchListItem);
        }

        nearbyPointsSearchListQueryBuilderView.setSearchList(interestPointsSearchListItems);
    }

    private String getAddressLineValue(){
        Editable editable = addressLine1EditText.getText();
        if(editable == null) return "";
        return editable.toString();
    }

    private String getPostalCodeValue(){
        Editable editable = postalCodeEditText.getText();
        if(editable == null) return "";
        return editable.toString();
    }

    private boolean isSearchAddressUsed() {
        String addressLine = getAddressLineValue();
        String postalCode = getPostalCodeValue();

        return !addressLine.isEmpty() || !postalCode.isEmpty();
    }

    @OnClick(R.id.searchButton)
    public void onSearchClick() {
        String query = buildSearchQuery();

        if(!canSearch){
            Toast.makeText(this, "Please enter a valid search criteria",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(QUERY_EXTRA, query);
        intent.putExtra(PARAMS_EXTRA, queryParams.toArray());

        startActivity(intent);
    }

    private String joinAddressProperties(){
        //JOIN AddressProperties + Address
        return " INNER JOIN " +
                "AddressProperties" +
                " as _addressProperties" +
                " ON _p.id = _addressProperties.propertyId" +
                " INNER JOIN " +
                "Address" +
                " as _address" +
                " ON _addressProperties.addressId = _address.id";
    }

    private String joinPropertyInterestPoints(){
        return " INNER JOIN " +
                "PropertyInterestPoints" +
                " as _propertyInterestPoints" +
                " ON _p.id = _propertyInterestPoints.propertyId";
    }

    private String joinMedia(){
        return " INNER JOIN " +
                "Media" +
                " as _media" +
                " ON _p.id = _media.propertyId";
    }

    /**
     * This function is called before where clause
     * @return sql query
     */
    private String headerOfQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT DISTINCT _p.* FROM Property as _p");

        // JOIN AddressProperties if necessary
        if(isSearchAddressUsed()){
            builder.append(joinAddressProperties());
        }

        // JOIN PropertyInterestPoints if necessary
        if(nearbyPointsSearchListQueryBuilderView.isUsed()){
            builder.append(joinPropertyInterestPoints());
        }

        // JOIN Media if necessary
        if(mediaSearchQueryBuilderView.isValidInputValues()){
            builder.append(joinMedia());
        }

        return builder.toString();
    }

    private String addAddressQuery(){
        String addressLine = getAddressLineValue();
        String postalCode = getPostalCodeValue();

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

        return addressQuery;
    }

    private void addTypeOfPropertyQuery(boolean canAddLink, StringBuilder builder){
        if(canAddLink){
            addAndLink(builder);
        }

        String typeOfPropertyQueryString = typeOfPropertySearchListQueryBuilderView
                .buildConditionsFromQuery("_p.propertyTypeId ", queryParams);
        builder.append(typeOfPropertyQueryString);
    }

    private void addPriceQuery(boolean canAddLink, StringBuilder builder){
        if(canAddLink){
            addAndLink(builder);
        }

        String priceQueryString = priceSearchQueryBuilderView
                .buildConditionsFromQuery("_p.price ", queryParams);
        builder.append(priceQueryString);
    }

    private void addSurfaceQuery(boolean canAddLink, StringBuilder builder){
        if(canAddLink){
            addAndLink(builder);
        }

        String surfaceQueryString = surfaceSearchQueryBuilderView
                .buildConditionsFromQuery("_p.area ", queryParams);
        builder.append(surfaceQueryString);
    }

    private void addNearbyPointsQuery(boolean canAddLink, StringBuilder builder){
        if(canAddLink){
            addAndLink(builder);
        }

        String nearbyPointsQueryString = nearbyPointsSearchListQueryBuilderView
                .buildConditionsFromQuery(
                        "_propertyInterestPoints.interestId ", queryParams);

        builder.append(nearbyPointsQueryString);
    }

    private void addEntryOfMarketQuery(boolean canAddLink, StringBuilder builder){
        if(canAddLink){
            addAndLink(builder);
        }

        String entryOfMarketQueryString = entryOfMarketSearchDateQueryBuilderView
                .buildConditionsFromQuery("_p.createdDate ", queryParams);
        builder.append(entryOfMarketQueryString);
    }

    private void addSoldQuery(boolean canAddLink, StringBuilder builder){
        if(canAddLink){
            addAndLink(builder);
        }

        String soldQueryString = soldSearchDateQueryBuilderView
                .buildConditionsFromQuery("_p.soldDate ", queryParams);
        builder.append(soldQueryString);
    }

    private boolean addMediaQuery(boolean canAddLink, StringBuilder builder){
        boolean canReplaceWhere = false;
        if(canAddLink){
            builder.append(addGroupBy("_p.id, _media.id"));
            builder.append(" HAVING ");
        }else{
            canReplaceWhere = true;
        }

        String mediaQueryString = mediaSearchQueryBuilderView
                .buildConditionsFromQuery(
                        "COUNT(_media.propertyId)", queryParams);
        builder.append(mediaQueryString);

        return canReplaceWhere;
    }

    private boolean buildWhereConditionsQuery(StringBuilder builder){
        boolean canAddLink = false;

        if(isSearchAddressUsed()) {
            builder.append(addAddressQuery());
            canAddLink = true;
        }

        if(surfaceSearchQueryBuilderView.isValidInputValues()){
            addSurfaceQuery(canAddLink, builder);
            canAddLink = true;
        }

        if(typeOfPropertySearchListQueryBuilderView.isUsed()){
            addTypeOfPropertyQuery(canAddLink, builder);
            canAddLink = true;
        }

        if(priceSearchQueryBuilderView.isValidInputValues()){
            addPriceQuery(canAddLink, builder);
            canAddLink = true;
        }

        if(nearbyPointsSearchListQueryBuilderView.isUsed()){
            addNearbyPointsQuery(canAddLink, builder);
            canAddLink = true;
        }

        if(entryOfMarketSearchDateQueryBuilderView.isUsed()){
            addEntryOfMarketQuery(canAddLink, builder);
            canAddLink = true;
        }

        if(soldSearchDateQueryBuilderView.isUsed()){
            addSoldQuery(canAddLink, builder);
            canAddLink = true;
        }

        // If canAddLink is true, we can do search (Where clause)
        canSearch = canAddLink;

        boolean canReplaceWhere = false;
        if(mediaSearchQueryBuilderView.isValidInputValues()){
            canReplaceWhere = addMediaQuery(canAddLink, builder);
        }

        if(canReplaceWhere){
            // If canReplaceWhere is true, we can do search (Having clause)
            canSearch = true;
        }

        return canReplaceWhere;
    }

    private String addHavingClauseForMediaQuery(boolean canReplaceWhere, String query){
        if(canReplaceWhere){
            String groupByMedia = addGroupBy("_p.id, _media.propertyId");
            query = query.replace("WHERE", groupByMedia + " HAVING");
        }

        return query;
    }

    private String buildSearchQuery() {
        // Clean query
        queryParams.clear();

        // Build sql query
        StringBuilder builder = new StringBuilder();
        builder.append(headerOfQuery());
        builder.append(" WHERE ");
        boolean canReplaceWhere = buildWhereConditionsQuery(builder);
        return addHavingClauseForMediaQuery(canReplaceWhere, builder.toString());
    }

    private String addGroupBy(String criteria){
        return " GROUP BY " +
                criteria;
    }

    private void addAndLink(StringBuilder builder){
        builder.append(" AND ");
    }
}
