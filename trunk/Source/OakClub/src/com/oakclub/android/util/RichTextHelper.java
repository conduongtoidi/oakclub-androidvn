package com.oakclub.android.util;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

public class RichTextHelper {
	public static SpannableStringBuilder getRichText(String text){
	    SpannableStringBuilder builder=new SpannableStringBuilder();     
	    String myText=text;
	    boolean done=false;
	    while(!done){
	        if((myText.indexOf("{{b}}")>=0) && (myText.indexOf("{{b}}")<myText.indexOf("{{/b}}"))){
	            int nIndex=myText.indexOf("{{b}}");
	            String normalText=myText.substring(0,nIndex);
	            builder.append(normalText);
	            myText=myText.substring(nIndex+5);
	        }else if((myText.indexOf("{{/b}}")>=0)){        
	            int bIndex=myText.indexOf("{{/b}}");
	            String boldText=myText.substring(0,bIndex);
	            builder.append(boldText);

	            myText=myText.substring(bIndex+6);
	            int start=builder.length()-boldText.length();
	            int end =builder.length();
	            if((start>=0) && (end>start)){
	                builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
	            }

	        }else{
	            if(myText.contains("{{/b}}"))
	                myText=myText.replace("{{/b}}", "");
	            builder.append(myText);
	            done=true;
	        }
	    }

	    return builder;
	}

}