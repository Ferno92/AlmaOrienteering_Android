package com.almaorient.ferno92.almaorienteering.recensioni;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.R;

/**
 * Created by lucas on 26/03/2017.
 */

class RecensioniBinder implements SimpleAdapter.ViewBinder {
    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if(view.getId() == R.id.rating_recensione){
            String stringval = (String) data;
            float ratingValue = Float.parseFloat(stringval);
            RatingBar ratingBar = (RatingBar) view;
            ratingBar.setRating(ratingValue);
            return true;
        }else if(view.getId() == R.id.testo_recensione){
            String stringval = (String) data;
            TextView recensioneText = (TextView) view;
            recensioneText.setText(stringval);
            return true;
        }else if(view.getId() == R.id.quota){
            String stringval = (String) String.valueOf(data);
            TextView quotaText = (TextView) view;
            quotaText.setText(stringval);
            return true;
        }else if(view.getId() == R.id.rec_up){
            final int resId = Integer.parseInt((String)data);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    LinearLayout parentView = (LinearLayout) view.getParent();
//                    TextView quotaText = (TextView) parentView.findViewById(R.id.quota);
//
//                    ImageView recUp = (ImageView)view;
//                    ImageView recDown = (ImageView)parentView.findViewById(R.id.rec_up);
//                    if(recUp.getTag() != resId && recDown.getTag() != resId){
//                        quotaText.setText(String.valueOf(Integer.parseInt(quotaText.getText().toString()) + 1));
//                        recUp.setImageResource(resId);
//                    }
//                }
//            });
            return true;

        }else if(view.getId() == R.id.rec_down){
            final int resId = Integer.parseInt((String)data);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    LinearLayout parentView = (LinearLayout) view.getParent();
//                    TextView quotaText = (TextView) parentView.findViewById(R.id.quota);
//                    ImageView recUp = (ImageView)parentView.findViewById(R.id.rec_up);
//                    ImageView recDown = (ImageView)view;
//                    if(Integer.parseInt(quotaText.getText().toString()) - 1 >= 0 &&
//                            recUp.getTag() != resId && recDown.getTag() != resId){
//                        quotaText.setText(String.valueOf(Integer.parseInt(quotaText.getText().toString()) - 1));
//                        recDown.setImageResource(resId);
//                    }
//                }
//            });
            return true;

        }
        return false;
    }

}
