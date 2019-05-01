package com.example.ananyagupta.trappyy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View;
import android.text.TextWatcher;
import android.text.Editable;

public class CurrencyConverterActivity extends AppCompatActivity {

    EditText rupeesText;
    TextView resulttext;
    Button convertBtn;
    Spinner currencySpinner;
    double rupees;
    double result;
    String symbol;
    String[] currency = {"Select", "USD", "EUR", "JPY", "GBP", "CHF" ,"AUD"};
    //int flags[] = {R.drawable.usa, R.drawable.eulogo, R.drawable.japan, R.drawable.uk,R.drawable.switz};
    //String[] cunit={"USD", "EUR", "JPY", "GBP", "CHF"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencyconverter);


        rupeesText = findViewById(R.id.rupeetext);
        resulttext = findViewById(R.id.resulttext);
        currencySpinner = findViewById(R.id.spinnerCurreny);


        ArrayAdapter currencyAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, currency);

        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);


        currencySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            int iCurrentSelection = currencySpinner.getSelectedItemPosition();
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                // Toast.makeText(getApplicationContext(), cunit[position], Toast.LENGTH_LONG).show();
                String rupees1 = rupeesText.getText().toString();
                String toCurrency = currencySpinner.getSelectedItem().toString();
                System.out.println("to..." + toCurrency);

                if (rupees1.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_LONG).show();

                }
                else {
                    rupees = Double.parseDouble(rupees1);
                    if (toCurrency.equals("Select")) {
                        Toast.makeText(getApplicationContext(), "Please select the conversion unit", Toast.LENGTH_LONG).show();

                    } else {

                        switch (toCurrency) {

                            case "USD":
                                result = 0.014 * rupees;
                                symbol = "$";
                                break;

                            case "EUR":
                                result = 0.012 * rupees;
                                symbol = "€";
                                break;

                            case "JPY":
                                result = 1.54 * rupees;
                                symbol = "¥";
                                break;

                            case "GBP":
                                result = 0.011 * rupees;
                                symbol = "£";
                                break;

                            case "CHF":
                                result = 0.014 * rupees;
                                symbol = "₣";
                                break;

                            case "AUD":
                                result = 0.02025 * rupees;
                                symbol = "AU$";
                                break;

                            default:
                                Toast.makeText(getApplicationContext(), "Please select the conversion unit", Toast.LENGTH_SHORT).show();

                        }
                        resulttext.setText("Amount : " + symbol + result);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        rupeesText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                String rupees1 = rupeesText.getText().toString();
                String toCurrency = currencySpinner.getSelectedItem().toString();
                System.out.println("to..." + toCurrency);

                if (rupees1.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_LONG).show();

                } else {
                    rupees = Double.parseDouble(rupees1);
                    if (toCurrency.equals("Select")) {
                        Toast.makeText(getApplicationContext(), "Please select the conversion unit", Toast.LENGTH_LONG).show();

                    } else {

                        switch (toCurrency) {

                            case "USD":
                                result = 0.014 * rupees;
                                symbol = "$";
                                break;

                            case "EUR":
                                result = 0.012 * rupees;
                                symbol = "€";
                                break;

                            case "JPY":
                                result = 1.54 * rupees;
                                symbol = "¥";
                                break;

                            case "GBP":
                                result = 0.011 * rupees;
                                symbol = "£";
                                break;

                            case "CHF":
                                result = 0.014 * rupees;
                                symbol = "₣";
                                break;

                            case "AUD":
                                result = 0.02025 * rupees;
                                symbol = "AU$";
                                break;

                            default:
                                Toast.makeText(getApplicationContext(), "Please select the conversion unit", Toast.LENGTH_SHORT).show();

                        }
                        resulttext.setText("Amount: " + symbol + result);
                        //pb.setProgress((int) result%100);
                        //sb1.setProgress((int) result);
                        //sb2.setProgress((int) result);
                    }
                }
                if(s.length()==0){
                    resulttext.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
