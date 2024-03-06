package com.company.onboarding.view.locationlookup;

import com.company.onboarding.entity.Location;
import com.company.onboarding.entity.LocationType;
import com.company.onboarding.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.valuepicker.EntityPicker;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.BaseAction;
import io.jmix.flowui.view.*;
import io.jmix.mapsflowui.component.GeoMap;
import io.jmix.mapsflowui.component.model.FitOptions;
import io.jmix.mapsflowui.component.model.source.DataVectorSource;
import io.jmix.mapsflowui.kit.component.model.Easing;
import io.jmix.mapsflowui.kit.component.model.Padding;
import io.jmix.mapsflowui.kit.component.model.style.Fill;
import io.jmix.mapsflowui.kit.component.model.style.Style;
import io.jmix.mapsflowui.kit.component.model.style.image.Anchor;
import io.jmix.mapsflowui.kit.component.model.style.image.IconOrigin;
import io.jmix.mapsflowui.kit.component.model.style.image.IconStyle;
import io.jmix.mapsflowui.kit.component.model.style.stroke.Stroke;
import io.jmix.mapsflowui.kit.component.model.style.text.TextStyle;
import org.locationtech.jts.geom.Geometry;

import java.util.Objects;

@Route(value = "LocationLookupView", layout = MainView.class)
@ViewController("LocationLookupView")
@ViewDescriptor("location-lookup-view.xml")
@DialogMode(width = "60em", height = "45em")
public class LocationLookupView extends StandardView {
    @ViewComponent
    private EntityPicker<Location> currentLocationField;
    @ViewComponent("map.buildingLayer.buildingSource")
    private DataVectorSource<Location> buildingSource;
    @ViewComponent
    private GeoMap map;
    @ViewComponent
    private BaseAction select;
    @ViewComponent("map.buildingAreaLayer.buildingAreaSource")
    private DataVectorSource<Location> buildingAreaSource;

    @Subscribe
    public void onInit(final InitEvent event) {
        initBuildingSource();
        initBuildingAreaSource();
    }

    private void initBuildingSource() {
        buildingSource.setStyleProvider(location -> new Style()
                .withImage(new IconStyle()
                        .withScale(0.5)
                        .withAnchorOrigin(IconOrigin.BOTTOM_LEFT)
                        .withAnchor(new Anchor(0.49, 0.12))
                        .withSrc(location.getType() == LocationType.OFFICE
                                ? "map-icons/office-marker.png"
                                : "map-icons/coworking-marker.png"))
                .withText(new TextStyle()
                        .withBackgroundFill(new Fill("rgba(255, 255, 255, 0.6)"))
                        .withPadding(new Padding(5, 5, 5, 5))
                        .withOffsetY(15)
                        .withFont("bold 15px sans-serif")
                        .withText(location.getCity())));

        buildingSource.addGeoObjectClickListener(clickEvent -> {
            Location location = clickEvent.getItem();

            setMapCenter(location.getBuilding());

            onLocationChanged(location);
        });
    }

    private void initBuildingAreaSource() {
        buildingAreaSource.setStyleProvider(location -> {
            String fillColor = location.getType() == LocationType.OFFICE
                    ? "rgba(52, 216, 0, 0.2)"
                    : "rgba(1, 147, 154, 0.2)";
            String strokeColor = location.getType() == LocationType.OFFICE
                    ? "#228D00"
                    : "#123EAB";
            return new Style()
                    .withFill(new Fill(fillColor))
                    .withStroke(new Stroke()
                            .withWidth(2d)
                            .withColor(strokeColor));
        });
    }

    @Subscribe("select")
    public void onSelect(final ActionPerformedEvent event) {
        close(StandardOutcome.SELECT);
    }

    public Location getSelected() {
        return currentLocationField.getValue();
    }

    public void setSelected(Location location) {
        currentLocationField.setValue(location);
    }

    private void onLocationChanged(Location newLocation) {
        if (!Objects.equals(currentLocationField.getValue(), newLocation)) {
            currentLocationField.setValue(newLocation);
            select.setEnabled(true);
        }
    }

    private void setMapCenter(Geometry geometry) {
        map.fit(new FitOptions(geometry)
                .withDuration(1000)
                .withEasing(Easing.LINEAR)
                .withMaxZoom(20d));
    }
}