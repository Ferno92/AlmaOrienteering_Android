package com.almaorient.unibo.almaorienteering.recensioni;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.almaorient.unibo.almaorienteering.R;

import java.util.HashMap;

/**
 * Created by lucas on 26/03/2017.
 */

class RecensioniBinder implements SimpleAdapter.ViewBinder {

    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if (view.getId() == R.id.rating_recensione) {
            String stringval = (String) data;
            float ratingValue = Float.parseFloat(stringval);
            RatingBar ratingBar = (RatingBar) view;
            ratingBar.setRating(ratingValue);
            return true;
        } else if (view.getId() == R.id.testo_recensione) {
            String stringval = (String) data;
            TextView recensioneText = (TextView) view;
            recensioneText.setText(stringval);
            return true;
        } else if (view.getId() == R.id.quota) {
            String stringval = (String) String.valueOf(data);
            TextView quotaText = (TextView) view;
            quotaText.setText(stringval);
            return true;
        } else if (view.getId() == R.id.rec_up) {
            final int resId = (int) ((HashMap) data).get("resourceId");
            final int defaultResId = (int) ((HashMap) data).get("defaultResourceId");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout parentView = (LinearLayout) view.getParent();
                    TextView quotaText = (TextView) parentView.findViewById(R.id.quota);

                    ImageView recUp = (ImageView) view;
                    ImageView recDown = (ImageView) parentView.findViewById(R.id.rec_down);
                    if ((Integer) (recUp.getTag() != null ? recUp.getTag() : 0) != resId &&
                            (Integer) (recDown.getTag() != null ? recDown.getTag() : 0) != resId) {
                        quotaText.setText(String.valueOf(Integer.parseInt(quotaText.getText().toString()) + 1));
                        recUp.setImageResource(resId);
                        recUp.setTag(resId);
                    }else if ((Integer) (recUp.getTag() != null ? recUp.getTag() : 0) == resId) {

                        quotaText.setText(String.valueOf(Integer.parseInt(quotaText.getText().toString()) - 1));
                        recUp.setImageResource(defaultResId);
                        recUp.setTag(defaultResId);

                    }else if ((Integer) (recDown.getTag() != null ? recDown.getTag() : 0) == resId) {

                        quotaText.setText(String.valueOf(Integer.parseInt(quotaText.getText().toString()) + 2));
                        recUp.setImageResource(resId);
                        recUp.setTag(resId);
                        recDown.setImageResource(defaultResId);
                        recDown.setTag(defaultResId);

                    }
                }
            });
            return true;

        } else if (view.getId() == R.id.rec_down) {
            final int resId = (int) ((HashMap) data).get("resourceId");
            final int defaultResId = (int) ((HashMap) data).get("defaultResourceId");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout parentView = (LinearLayout) view.getParent();
                    TextView quotaText = (TextView) parentView.findViewById(R.id.quota);
                    ImageView recUp = (ImageView) parentView.findViewById(R.id.rec_up);
                    ImageView recDown = (ImageView) view;
                    if (Integer.parseInt(quotaText.getText().toString()) - 1 >= 0 &&
                            (Integer) (recUp.getTag() != null ? recUp.getTag() : 0) != resId &&
                            (Integer) (recDown.getTag() != null ? recDown.getTag() : 0) != resId) {
                        quotaText.setText(String.valueOf(Integer.parseInt(quotaText.getText().toString()) - 1));
                        recDown.setImageResource(resId);
                        recDown.setTag(resId);
                    } else if ((Integer) (recDown.getTag() != null ? recDown.getTag() : 0) == resId) {

                        quotaText.setText(String.valueOf(Integer.parseInt(quotaText.getText().toString()) + 1));
                        recDown.setImageResource(defaultResId);
                        recDown.setTag(defaultResId);
                    }else if ((Integer) (recUp.getTag() != null ? recUp.getTag() : 0) == resId) {

                        if(Integer.parseInt(quotaText.getText().toString()) - 1 >= 1){
                            quotaText.setText(String.valueOf(Integer.parseInt(quotaText.getText().toString()) - 2));
                            recDown.setImageResource(resId);
                            recDown.setTag(resId);
                        }else{
                            quotaText.setText(String.valueOf(Integer.parseInt(quotaText.getText().toString()) - 1));
                        }
                        recUp.setImageResource(defaultResId);
                        recUp.setTag(defaultResId);

                    }
                }
            });
            return true;

        }
        return false;
    }

}
