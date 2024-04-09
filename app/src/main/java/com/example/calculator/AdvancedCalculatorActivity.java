package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.Objects;

public class AdvancedCalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView advancedTextView;
    private StringBuilder advancedInput = new StringBuilder();
    private static final String RESULT_TEXT_KEY = "result_text";
    private boolean equalsBeenPressed;

    private double advancedResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_calculator);

        advancedTextView = findViewById(R.id.advancedTextView);


        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString(RESULT_TEXT_KEY);
            advancedTextView.setText(savedText);
            advancedInput.append(savedText);
        }

        findViewById(R.id.advancedNumZeroButton).setOnClickListener(this);
        findViewById(R.id.advancedNumOneButton).setOnClickListener(this);
        findViewById(R.id.advancedNumTwoButton).setOnClickListener(this);
        findViewById(R.id.advancedNumThreeButton).setOnClickListener(this);
        findViewById(R.id.advancedNumFourButton).setOnClickListener(this);
        findViewById(R.id.advancedNumFiveButton).setOnClickListener(this);
        findViewById(R.id.advancedNumSixButton).setOnClickListener(this);
        findViewById(R.id.advancedNumSevenButton).setOnClickListener(this);
        findViewById(R.id.advancedNumEightButton).setOnClickListener(this);
        findViewById(R.id.advancedNumNineButton).setOnClickListener(this);
        findViewById(R.id.advancedAdditionButton).setOnClickListener(this);
        findViewById(R.id.advancedSubtractionButton).setOnClickListener(this);
        findViewById(R.id.advancedMultiplicationButton).setOnClickListener(this);
        findViewById(R.id.advancedDivisionButton).setOnClickListener(this);
        findViewById(R.id.advancedEqualsButton).setOnClickListener(this);
        findViewById(R.id.advancedFloatingPointButton).setOnClickListener(this);
        findViewById(R.id.advancedBkspButton).setOnClickListener(this);
        findViewById(R.id.advancedCCEButton).setOnClickListener(this);
        findViewById(R.id.advancedPlusMinusButton).setOnClickListener(this);
        findViewById(R.id.advancedSinButton).setOnClickListener(this);
        findViewById(R.id.advancedCosButton).setOnClickListener(this);
        findViewById(R.id.advancedTanButton).setOnClickListener(this);
        findViewById(R.id.advancedNatLogButton).setOnClickListener(this);
        findViewById(R.id.advancedSquareRootButton).setOnClickListener(this);
        findViewById(R.id.advancedPowerTwoButton).setOnClickListener(this);
        findViewById(R.id.advancedPowerYButton).setOnClickListener(this);
        findViewById(R.id.advancedLogButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String buttonText = button.getText().toString();
        int viewID = v.getId();


        int inputLength = advancedInput.length() - 1;

        if((advancedInput.toString().equals("Infinity") ||
                advancedInput.toString().equals("-Infinity") ||
                advancedInput.toString().equals("NaN")) && viewID != R.id.advancedCCEButton){
            Toast.makeText(this, "No operations allowed on that", Toast.LENGTH_SHORT).show();
            return;
        }

        if (viewID == R.id.advancedBkspButton) {
            if(advancedInput.toString().contains("E")){
                return;
            }
            if (advancedInput.length() == 0) {
                return;
            }
            if (advancedInput.charAt(0) == '-' && advancedInput.length() == 1) {
                advancedInput.deleteCharAt(advancedInput.length() - 1);
                advancedTextView.setText(advancedInput);
                return;
            }
            if (!advancedInput.toString().isEmpty()) {
                advancedInput.deleteCharAt(advancedInput.length() - 1);
                advancedTextView.setText(advancedInput);
                return;
            }

        } else if (viewID == R.id.advancedCCEButton) {
            advancedInput.setLength(0);
        }
//==================================================================================================
//==================================================================================================

        else if (viewID == R.id.advancedPlusMinusButton && advancedInput.length() > 0) {
            if (advancedInput.charAt(0) == '-' && advancedInput.length() == 1) {
                return;
            }
            int operatorIndex = -1;
            for (int i = inputLength; i >= 0; i--) {
                if (isOperator(advancedInput.charAt(i))) {
                    operatorIndex = i;
                    break;
                }
            }
            String[] substrings = advancedInput.toString().split("[-+*/()^]");
            String lastNumber = substrings[substrings.length - 1];
            if (isOperator(advancedInput.charAt(inputLength)) || Objects.equals(lastNumber, "0")) {
                return;
            }


            if (operatorIndex == -1) {
                advancedInput.insert(0, "-");
            } else if (operatorIndex == 0) {
                if (advancedInput.charAt(0) == '-') {
                    advancedInput.deleteCharAt(0);
                }
            } else if (advancedInput.charAt(operatorIndex) == '-' && advancedInput.charAt(operatorIndex - 1) == '(') {
                advancedInput.deleteCharAt(operatorIndex - 1);
                advancedInput.deleteCharAt(operatorIndex - 1);
                advancedInput.deleteCharAt(operatorIndex + lastNumber.length() - 1);
            } else if (advancedInput.charAt(operatorIndex) == '+') {
                advancedInput.insert(operatorIndex + 1, "(-");
                advancedInput.append(')');
            } else if ((advancedInput.charAt(operatorIndex + 1) == '-')) {
                advancedInput.deleteCharAt(operatorIndex);
                advancedInput.deleteCharAt(operatorIndex);
                advancedInput.deleteCharAt(operatorIndex + lastNumber.length() - 1);
            } else {
                advancedInput.insert(operatorIndex + 1, "(-");
                advancedInput.append(')');
            }
        }
//==================================================================================================
//==================================================================================================

        else if (viewID == R.id.advancedFloatingPointButton) {
            if (!advancedInput.toString().isEmpty() &&
                    advancedInput.charAt(inputLength) != ')' &&
                    !isOperator(advancedInput.charAt(inputLength))) {
                String[] substrings = advancedInput.toString().split("[-+*/()^]");
                String lastNumber = substrings[substrings.length - 1];
                if (!lastNumber.contains(".")) {
                    advancedInput.append(buttonText);
                }
            }
        }
//==================================================================================================
//==================================================================================================
        else if (validateOperators(viewID) || isDigit(viewID)) {
            if (isDigit(viewID) && equalsBeenPressed) {
                advancedInput.setLength(0);
            } else {
                equalsBeenPressed = false;
            }

            if (advancedInput.length() == 1) {
                if (advancedInput.charAt(0) == '-' && isDigit(viewID)) {
                    advancedInput.append(buttonText);
                    advancedTextView.setText(advancedInput.toString());
                    return;
                }
            }

            String[] substrings = advancedInput.toString().split("[-+*/()^]");
            String lastNumber = substrings[substrings.length - 1];
            if (isDigit(viewID) && lastNumber.equals("0") && !isOperator(advancedInput.charAt(advancedInput.length() - 1))) {
                advancedInput.deleteCharAt(advancedInput.length() - 1);
                advancedInput.append(buttonText);
                advancedTextView.setText(advancedInput.toString());
                return;
            } else if (isDigit(viewID) && lastNumber.equals("0") && lastNumber.contains(".")) {
                advancedInput.append(buttonText);
                advancedTextView.setText(advancedInput.toString());
                return;
            } else if (isDigit(viewID) && !lastNumber.contains(".") && lastNumber.equals("0")
                    && !isOperator(advancedInput.charAt(advancedInput.length() - 1))) {
                advancedInput.deleteCharAt(advancedInput.length() - 1);
                advancedInput.append(buttonText);
                advancedTextView.setText(advancedInput.toString());
                return;
            } else if (validateOperators(viewID) && viewID != R.id.advancedPowerYButton) {
                advancedInput.append(buttonText);
                advancedTextView.setText(advancedInput.toString());
                return;
            }

            if (isDigit(viewID) && !advancedInput.toString().isEmpty()) {
                if (advancedInput.charAt(inputLength) == ')') {
                    return;
                }
            }
            if (viewID != R.id.advancedPowerYButton) {
                advancedInput.append(buttonText);
            }
        }
//==================================================================================================
//==================================================================================================
        if (viewID == R.id.advancedEqualsButton) {
            if (advancedInput.toString().contains("^")) {
                String[] pows = advancedInput.toString().split("\\^");

                advancedInput.setLength(0);
                advancedInput.append(pows[0]);
                if(supportCalc() == -1){
                    advancedInput.setLength(0);
                    advancedTextView.setText(advancedInput.toString());
                    return;
                }
                double pow1 = advancedResult;

                advancedInput.setLength(0);
                advancedInput.append(pows[1]);
                if(supportCalc() == -1){
                    advancedInput.setLength(0);
                    advancedTextView.setText(advancedInput.toString());
                    return;
                }
                double pow2 = advancedResult;

                double powRes = Math.pow(pow1, pow2);
                advancedInput.setLength(0);
                advancedInput.append(powRes);
                advancedTextView.setText(advancedInput.toString());
            } else if(advancedInput.charAt(0) != '-' && advancedInput.length() != 1) {
                calculateResult();
            }
            equalsBeenPressed = true;
        } else if (viewID == R.id.advancedSinButton && advancedInput.length() > 0) {
            if(advancedInput.charAt(0) == '-' && advancedInput.length() == 1){
                return;
            }
            if (supportCalc() == -1) {
                return;
            }
            double sinValue = Math.sin(Math.toRadians(advancedResult));
            advancedInput.setLength(0);
            advancedInput.append(sinValue);
            advancedTextView.setText(advancedInput.toString());
            equalsBeenPressed = true;

        } else if (viewID == R.id.advancedCosButton && advancedInput.length() > 0) {
            if(advancedInput.charAt(0) == '-' && advancedInput.length() == 1){
                return;
            }
            if (supportCalc() == -1) {
                return;
            }
            double cosValue = Math.cos(Math.toRadians(advancedResult));
            advancedInput.setLength(0);
            advancedInput.append(cosValue);
            advancedTextView.setText(advancedInput.toString());
            equalsBeenPressed = true;

        } else if (viewID == R.id.advancedTanButton && advancedInput.length() > 0) {
            if(advancedInput.charAt(0) == '-' && advancedInput.length() == 1){
                return;
            }
            if (supportCalc() == -1) {
                return;
            }
            if (Math.abs(advancedResult % 180) == 90) {
                Toast.makeText(this, "Tangent is undefined for this value", Toast.LENGTH_SHORT).show();
            } else {
                double tanValue = Math.tan(Math.toRadians(advancedResult));
                advancedInput.setLength(0);
                advancedInput.append(tanValue);
                advancedTextView.setText(advancedInput.toString());
                equalsBeenPressed = true;
            }
        } else if (viewID == R.id.advancedNatLogButton && advancedInput.length() > 0) {
            if(advancedInput.charAt(0) == '-' && advancedInput.length() == 1){
                return;
            }
            if (supportCalc() == -1) {
                return;
            }
            if (advancedResult <= 0) {
                Toast.makeText(this, "Value must be greater than 0 for natural logarithm", Toast.LENGTH_SHORT).show();
            } else {
                double natLogValue = Math.log(advancedResult);
                advancedInput.setLength(0);
                advancedInput.append(natLogValue);
                advancedTextView.setText(advancedInput.toString());
                equalsBeenPressed = true;
            }
        } else if (viewID == R.id.advancedLogButton && advancedInput.length() > 0) {
            if(advancedInput.charAt(0) == '-' && advancedInput.length() == 1){
                return;
            }
            if (supportCalc() == -1) {
                return;
            }
            if (advancedResult <= 0) {
                Toast.makeText(this, "Value must be greater than 0 for logarithm", Toast.LENGTH_SHORT).show();
            } else {
                double logValue = Math.log10(advancedResult);
                advancedInput.setLength(0);
                advancedInput.append(logValue);
                advancedTextView.setText(advancedInput.toString());
                equalsBeenPressed = true;
            }
        } else if (viewID == R.id.advancedPowerTwoButton && advancedInput.length() > 0) {
            if(advancedInput.charAt(0) == '-' && advancedInput.length() == 1){
                return;
            }
            if (supportCalc() == -1) {
                return;
            }
            double powerTwoValue = Math.pow(advancedResult, 2);
            advancedInput.setLength(0);
            advancedInput.append(powerTwoValue);
            advancedTextView.setText(advancedInput.toString());
            equalsBeenPressed = true;
        } else if (viewID == R.id.advancedPowerYButton) {
            if (advancedInput.length() > 0) {
                if (advancedInput.charAt(advancedInput.length() - 1) != '^' &&
                        !isOperator(advancedInput.charAt(advancedInput.length() - 1)) &&
                        !advancedInput.toString().contains("^")) {
                    if(!advancedInput.toString().equals("Infinity") &&
                            !advancedInput.toString().equals("-Infinity") &&
                            !advancedInput.toString().equals("NaN")) {
                        advancedInput.append('^');
                    }
                }
            }
        } else if (viewID == R.id.advancedSquareRootButton && advancedInput.length() > 0) {
            if(advancedInput.charAt(0) == '-' && advancedInput.length() == 1){
                return;
            }
            if (supportCalc() == -1) {
                return;
            }
            if (advancedResult < 0) {
                Toast.makeText(this, "Cannot calculate square root of negative number", Toast.LENGTH_SHORT).show();
            } else {
                double sqrtValue = Math.sqrt(advancedResult);
                advancedInput.setLength(0);
                advancedInput.append(sqrtValue);
                advancedTextView.setText(advancedInput.toString());
                equalsBeenPressed = true;
            }
        } else {
            equalsBeenPressed = false;
        }
        if (viewID != R.id.advancedEqualsButton) {
            advancedTextView.setText(advancedInput.toString());
        }
    }
//==================================================================================================
//==================================================================================================
    private void calculateResult() {
        try {
            Context rhino = Context.enter();
            rhino.setOptimizationLevel(-1);

            Scriptable scope = rhino.initStandardObjects();

            if (isOperator(advancedInput.charAt(advancedInput.length() - 1))) {
                advancedInput.deleteCharAt(advancedInput.length() - 1);
            }
            if (advancedInput.toString().contains("/0") && !advancedInput.toString().contains("/0.")) {
                Toast.makeText(this, "Can't divide by zero", Toast.LENGTH_SHORT).show();
                return;
            }
            Object result = rhino.evaluateString(scope, advancedInput.toString(), "JavaScript", 1, null);
            if (result.toString().equals("NaN") ||
                    result.toString().equals("-Infinity") ||
                    result.toString().equals("Infinity")) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                return;
            }
            advancedInput.setLength(0);
            if (result.toString().charAt(result.toString().length() - 2) == '.' &&
                    result.toString().charAt(result.toString().length() - 1) == '0') {
                String new_result = result.toString();
                new_result = new_result.substring(0, new_result.length() - 2);
                advancedInput.append(new_result);
                advancedTextView.setText(new_result);
            } else {
                advancedInput.append(result);
                advancedTextView.setText(result.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int supportCalc() {
        try {
            Context rhino = Context.enter();
            rhino.setOptimizationLevel(-1);

            Scriptable scope = rhino.initStandardObjects();

            if (isOperator(advancedInput.charAt(advancedInput.length() - 1))) {
                advancedInput.deleteCharAt(advancedInput.length() - 1);
            }

            if (advancedInput.toString().contains("/0") && !advancedInput.toString().contains("/0.")) {
                Toast.makeText(this, "Can't divide by zero", Toast.LENGTH_SHORT).show();
                return -1;
            }

            Object result = rhino.evaluateString(scope, advancedInput.toString(), "JavaScript", 1, null);
            if (result.toString().equals("NaN") ||
                    result.toString().equals("-Infinity") ||
                    result.toString().equals("Infinity")) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                return -1;
            }
            advancedResult = Double.parseDouble(result.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private boolean validateOperators(int buttonID) {
        String inputText = advancedInput.toString();

        if (inputText.isEmpty()) {
            return false;
        } else if (!isOperator(inputText.charAt(inputText.length() - 1)) && !isFloatingpoint(inputText.charAt(inputText.length() - 1))) {
            if (buttonID == R.id.advancedAdditionButton ||
                    buttonID == R.id.advancedSubtractionButton ||
                    buttonID == R.id.advancedMultiplicationButton ||
                    buttonID == R.id.advancedDivisionButton ||
                    buttonID == R.id.advancedPowerYButton ||
                    buttonID == R.id.advancedFloatingPointButton) {
                return true;
            }
        }
        return false;
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '^' || c == '/';
    }

    private boolean isFloatingpoint(char c) {
        return c == '.';
    }

    private boolean isDigit(int buttonID) {
        return buttonID == R.id.advancedNumZeroButton ||
                buttonID == R.id.advancedNumOneButton ||
                buttonID == R.id.advancedNumTwoButton ||
                buttonID == R.id.advancedNumThreeButton ||
                buttonID == R.id.advancedNumFourButton ||
                buttonID == R.id.advancedNumFiveButton ||
                buttonID == R.id.advancedNumSixButton ||
                buttonID == R.id.advancedNumSevenButton ||
                buttonID == R.id.advancedNumEightButton ||
                buttonID == R.id.advancedNumNineButton;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RESULT_TEXT_KEY, advancedTextView.getText().toString());
    }
}