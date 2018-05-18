package sdk_sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinspace.csevent.R;

import java.text.SimpleDateFormat;

import sdk_sample.sdk.Consts;

/**
 *第三方自定义改界面，此是个参考样式
 */
public class RamdonPasswordActivity extends Activity {
	
	private String randompw;
	private long deadline;
	private TextView tv_randompw,tv_deadline;
	private LinearLayout ll_password_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ramdon_password);
		tv_randompw=(TextView) findViewById(R.id.tv_randompw);
		tv_deadline=(TextView) findViewById(R.id.tv_deadline);
		ll_password_back = (LinearLayout) findViewById(R.id.ll_password_back);
		ll_password_back.setOnClickListener(clickListener);

		if(getIntent()!=null){
			randompw=getIntent().getStringExtra(Consts.SAYEE_RANDOM_PASSWORD);
			deadline=getIntent().getLongExtra(Consts.SAYEE_RANDOM_PASSWORD_DEADLINE,0);
		}
		
		tv_randompw.setText(randompw);
		if(deadline!=0)
			tv_deadline.setText("此密码于"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(deadline * 1000)+"过期失效");
	}
	
	
	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.ll_password_back:
					RamdonPasswordActivity.this.finish();
					break;
			}
		}
	};

}
