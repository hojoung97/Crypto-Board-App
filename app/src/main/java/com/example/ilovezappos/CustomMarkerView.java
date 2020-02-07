package com.example.ilovezappos;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

public class CustomMarkerView extends MarkerView {

    // Views
    private TextView priceValue;
    private TextView idValue;
    private TextView dateValue;

    // data arrays
    private String[] prices;
    private String[] ids;
    private String[] dates;

    public CustomMarkerView(Context context, int layoutResource,
                            String[] prices, String[] ids, String[] dates) {
        super(context, layoutResource);

        priceValue = findViewById(R.id.marker_price_text);
        idValue = findViewById(R.id.marker_id_text);
        dateValue = findViewById(R.id.marker_date_text);

        this.prices = prices;
        this.ids = ids;
        this.dates = dates;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);

        int index = (int)e.getX();
        priceValue.setText("Price: " + prices[index]);
        idValue.setText("ID: " + ids[index]);
        dateValue.setText("Date: " + dates[index]);
    }

    @Override
    public void setOffset(float offsetX, float offsetY) {
        offsetX = -(getWidth() / 2f);
        offsetY = -1.5f * getHeight();
        super.setOffset(offsetX, offsetY);
    }
}
