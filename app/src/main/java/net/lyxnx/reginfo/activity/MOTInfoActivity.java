package net.lyxnx.reginfo.activity;

import android.os.Bundle;
import android.text.Spanned;
import android.widget.ExpandableListView;
import net.lyxnx.reginfo.R;
import net.lyxnx.reginfo.reg.Utils;
import net.lyxnx.reginfo.reg.mot.MOTInfo;
import net.lyxnx.reginfo.reg.mot.ResultAdapter;

import static net.lyxnx.reginfo.reg.Utils.setText;

public class MOTInfoActivity extends InfoActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mothistory);
    
        MOTInfo info = (MOTInfo) getIntent().getSerializableExtra("info");
        
        ResultAdapter adapter = new ResultAdapter(this, info.getResults());
        ((ExpandableListView) findViewById(R.id.motHistory)).setAdapter(adapter);
     
        Spanned text = Utils.htmlify(getString(R.string.mot_valid, info.getDue()));
        text = Utils.changeFont(text, info.getDue(), 32);
        setText(findViewById(R.id.motDue), text);
    }
}