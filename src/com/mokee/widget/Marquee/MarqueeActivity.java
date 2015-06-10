package com.mokee.widget.Marquee;

import com.mokee.tools.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MarqueeActivity extends Activity implements OnClickListener {
	
	private MyMarquee marquee;
	private Button btn_Speed;
	private Button btn_Direction;
	private Button btn_Text;
	private Button btn_Color;
	private Button btn_Stop;
	
	private EditText et_Speed;
	private EditText et_Text;
	private EditText et_Direction;
	private EditText et_Color;
	
	private String input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marquee);
		
		marquee = (MyMarquee) findViewById(R.id.marquee_Text);
		btn_Speed = (Button) findViewById(R.id.btn_Speed);
		btn_Direction = (Button) findViewById(R.id.btn_Direction);
		btn_Text = (Button) findViewById(R.id.btn_Text);
		btn_Color = (Button) findViewById(R.id.btn_Color);
		btn_Stop = (Button) findViewById(R.id.btn_Stop);
		
		et_Speed = (EditText) findViewById(R.id.et_Speed);
		et_Text = (EditText) findViewById(R.id.et_Text);
		et_Direction = (EditText) findViewById(R.id.et_Direction);
		et_Color = (EditText) findViewById(R.id.et_Color);
		
		btn_Speed.setOnClickListener(this);
		btn_Direction.setOnClickListener(this);
		btn_Text.setOnClickListener(this);
		btn_Color.setOnClickListener(this);
		btn_Stop.setOnClickListener(this);
		
		et_Speed.setText(String.valueOf((int) marquee.getSpeed()));
		et_Direction.setText(String.valueOf(marquee.getDirection()));
		et_Color.setText(marquee.getColor() + "");
		
		if(marquee.getIsStarting()){
			btn_Stop.setText("Stop");
		} else {
			btn_Stop.setText("Start");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_Speed:
			input = et_Speed.getText().toString().trim();
			if (input != null && Integer.valueOf(input) > 0){
				marquee.setSpeed(Integer.valueOf(input));
			}
			break;
			
		case R.id.btn_Direction:
			input = et_Direction.getText().toString().trim();
			if(input != null && Integer.valueOf(input) == 0 || Integer.valueOf(input) == 1){
				marquee.setDirection(Integer.valueOf(input));
			}
			break;
			
		case R.id.btn_Text:
			input = et_Text.getText().toString().trim();
			if(input != null && !input.equals("")){
				marquee.setText(input);
			}
			break;
			
		case R.id.btn_Color:
			input = et_Color.getText().toString().trim();
			if(input != null && input.startsWith("0x") || input.startsWith("0X") && input.length() == 10){
				marquee.setColor(Integer.valueOf(input));
			}
			break;
			
		case R.id.btn_Stop:
			if(marquee.getIsStarting()){
				marquee.setIsStarting(false);
				btn_Stop.setText("Start");
			} else {
				marquee.setIsStarting(true);
				btn_Stop.setText("Stop");
			}
			break;

		default:
			break;
}		
	}
}
