package codepath.project.tipcalculator;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/** main Activity class for the Tip Calculator
*
* @author Dipankar
* @version 1 Jun 8, 2014.
*/
public class MainActivity extends Activity {
	// constants used when saving/restoring state
	private static final String BILL_TOTAL = "BILL_TOTAL";
	private static final String SPLIT_TOTAL = "SPLIT_TOTAL";
	private static final String CUSTOM_PERCENT = "CUSTOM_PERCENT";

	private double currentBillTotal; // bill amount entered by the user
	private int splitter; // Number of person paying the bill
	private int currentTipSelectorPercent; // tip % set with the SeekBar
	private EditText etTip10; // displays 10% tip
	private EditText etTotal10; // displays total with 10% tip
	private EditText etTip15; // displays 15% tip
	private EditText etTotal15; // displays total with 15% tip
	private EditText etbill; // accepts user input for bill total
	private EditText ettip20; // displays 20% tip
	private EditText etTotal20; // displays total with 20% tip
	private TextView tvTipSelector; // displays custom tip percentage
	private EditText etTipCustom; // displays custom tip amount
	private EditText etTotalCustom; // displays total with custom tip
	private EditText etSplit;
	private EditText etTotalPerPerson;
	
	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); // call superclass's version
		setContentView(R.layout.activity_main); // inflate the GUI

		// check if app just started or is being restored from memory
		if (savedInstanceState == null) // the app just started running
		{
			currentBillTotal = 0.0; // initialize the bill amount to zero
			currentTipSelectorPercent = 5; // initialize the custom tip to 5%
			splitter = 1; // initialize no. of per
		} 
		else // app is being restored from memory, not executed from scratch
		{
			// initialize the bill amount to saved amount
			currentBillTotal = savedInstanceState.getDouble(BILL_TOTAL);
			// initialize the no. of person paying the bill
			splitter = savedInstanceState.getInt(SPLIT_TOTAL);

			// initialize the custom tip to saved tip percent
			currentTipSelectorPercent = savedInstanceState.getInt(CUSTOM_PERCENT);
		} 

		// get references to the 10%, 15% and 20% tip and total EditTexts
		etTip10 = (EditText) findViewById(R.id.tip10EditText);
		etTotal10 = (EditText) findViewById(R.id.total10EditText);
		etTip15 = (EditText) findViewById(R.id.tip15EditText);
		etTotal15 = (EditText) findViewById(R.id.total15EditText);
		ettip20 = (EditText) findViewById(R.id.tip20EditText);
		etTotal20 = (EditText) findViewById(R.id.total20EditText);

		// get the TextView displaying the tip selector percentage
		tvTipSelector = (TextView) findViewById(R.id.customTipTextView);

		// get the custom tip and total EditTexts
		etTipCustom = (EditText) findViewById(R.id.totalTip);
		etTotalCustom = (EditText) findViewById(R.id.totalToPay);
		etSplit = (EditText) findViewById(R.id.etSplit);
		etTotalPerPerson = (EditText) findViewById(R.id.totalPerPerson);

		// get the bill amount
		etbill = (EditText) findViewById(R.id.billEditText);

		// billEditTextWatcher handles bill amount onTextChanged event
		etbill.addTextChangedListener(billEditTextWatcher);

		// splitEditTextWatcher handles no of contributor(s) onTextChanged event
		etSplit.addTextChangedListener(splitCustomEditTextWatcher);

		// get the SeekBar used to set the custom tip amount
		SeekBar customSeekBar = (SeekBar) findViewById(R.id.customSeekBar);
		customSeekBar.setOnSeekBarChangeListener(customSeekBarListener);
	} // end method onCreate

	// updates 10, 15 and 20 percent tip EditTexts
	private void updateStandard() {
		// calculate bill total with a ten percent tip
		double tenPercentTip = currentBillTotal * .1;
		double tenPercentTotal = currentBillTotal + tenPercentTip;

		etTip10.setText(String.format("%.02f", tenPercentTip));
		etTotal10.setText(String.format("%.02f", tenPercentTotal));

		// calculate bill total with a fifteen percent tip
		double fifteenPercentTip = currentBillTotal * .15;
		double fifteenPercentTotal = currentBillTotal + fifteenPercentTip;

		// set tipFifteenEditText's text to fifteenPercentTip
		etTip15.setText(String.format("%.02f", fifteenPercentTip));

		// set totalFifteenEditText's text to fifteenPercentTotal
		etTotal15.setText(String.format("%.02f", fifteenPercentTotal));

		// calculate bill total with a twenty percent tip
		double twentyPercentTip = currentBillTotal * .20;
		double twentyPercentTotal = currentBillTotal + twentyPercentTip;

		// set tipTwentyEditText's text to twentyPercentTip
		ettip20.setText(String.format("%.02f", twentyPercentTip));

		// set totalTwentyEditText's text to twentyPercentTotal
		etTotal20.setText(String.format("%.02f", twentyPercentTotal));
	} // end method updateStandard

	// updates the custom tip and total EditTexts
	private void updateCustom() {
		// set tvTipSelector's text to match the position of the SeekBar
		tvTipSelector.setText(currentTipSelectorPercent + "%");

		// calculate the custom tip amount
		double customTipAmount = currentBillTotal * currentTipSelectorPercent * .01;

		// calculate the total bill, including the custom tip
		double customTotalAmount = currentBillTotal + customTipAmount;

		// display the tip and total bill amounts
		etTipCustom.setText(String.format("%.02f", customTipAmount));
		etTotalCustom.setText(String.format("%.02f", customTotalAmount));
		double splittedAmount = customTotalAmount/splitter;
		///
		etTotalPerPerson.setText(String.format("%.02f", splittedAmount));
	} // end method updateCustom

	// save values of bill amount and tip selector
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putDouble(BILL_TOTAL, currentBillTotal);
		outState.putInt(SPLIT_TOTAL, splitter);
		outState.putInt(CUSTOM_PERCENT, currentTipSelectorPercent);
	} // end method onSaveInstanceState

	// called when the user changes the position of SeekBar
	private OnSeekBarChangeListener customSeekBarListener = new OnSeekBarChangeListener() {
		// update currentTipSelectorPercent, then call updateCustom
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// sets currentTipSelectorPercent to position of the SeekBar's thumb
			currentTipSelectorPercent = seekBar.getProgress();
			updateCustom(); // update EditTexts for custom tip and total
		} // end method onProgressChanged

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		} // end method onStartTrackingTouch

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		} // end method onStopTrackingTouch
	}; // end OnSeekBarChangeListener

	// event-handling object that responds to etbill's events
	private TextWatcher billEditTextWatcher = new TextWatcher() {
		// called when the user enters a number
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// convert etbill's text to a double
			try {
				currentBillTotal = Double.parseDouble(s.toString());
			} // end try
			catch (NumberFormatException e) {
				currentBillTotal = 0.0; // default if an exception occurs
			} // end catch

			// update the standard and custom tip EditTexts
			updateStandard(); // update the 10, 15 and 20% EditTexts
			updateCustom(); // update the custom tip EditTexts
		} // end method onTextChanged

		@Override
		public void afterTextChanged(Editable s) {
		} // end method afterTextChanged

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		} // end method beforeTextChanged
	}; // end billEditTextWatcher

	// event-handling object that responds to splitEditText's events
	private TextWatcher splitCustomEditTextWatcher = new TextWatcher() {
		// called when the user enters a number
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// convert splitEditText's text to an int
			try {
				splitter = Integer.parseInt(s.toString());
			} catch (NumberFormatException e) {
				splitter = 1; // default if an exception occurs
			} // end catch

			// update the standard and custom tip EditTexts
			updateStandard(); // update the 10, 15 and 20% EditTexts
			updateCustom(); // update the custom tip EditTexts
		} // end method onTextChanged

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

}