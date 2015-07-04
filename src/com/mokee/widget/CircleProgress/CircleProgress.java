package com.mokee.widget.CircleProgress;

import com.mokee.tools.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class CircleProgress {
	/** 
     * User-Defined：Circle Progress Dialog Round-Text Prompt-No Progress Number
     * @param Context 
     * @param Text Prompt 
     * @return Return "Dialog",can be called directly display.
     */  
    public static Dialog createCircleProgressDialog(Context context, String msg) {  
  
    	/** Loading layout */
        LayoutInflater inflater = LayoutInflater.from(context);  
        View v = inflater.inflate(R.layout.circle_progress_activity, null); 
        FrameLayout layout = (FrameLayout) v.findViewById(R.id.dialog_view);
        
        /** Get component (ImageView & TextView) */
        ImageView image = (ImageView) v.findViewById(R.id.iv_CircleProgress_Image);  
        TextView text = (TextView) v.findViewById(R.id.tv_CircleProgress_Text);
        
        /** Loading animation */
        Animation animation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.circle_progress_loading_animation);  
        /** Show animation and text*/
        image.startAnimation(animation);  
        text.setText(msg);
  
        /** Create dialog and define the style */
        Dialog dialog = new Dialog(context, R.style.circle_progress_loading_dialog);
        /** Set properties (Cancelable & LayoutParams(LinearLayout.LayoutParams.FILL_PARENT == -1)) */
        dialog.setCancelable(false);
        dialog.setContentView(layout, new LayoutParams(-1,-1));
        
        return dialog;  
    }  
}
