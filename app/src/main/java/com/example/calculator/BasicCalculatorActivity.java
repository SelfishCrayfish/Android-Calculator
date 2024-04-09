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

public class BasicCalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView basicTextView;
    private StringBuilder basicInput = new StringBuilder();
    private static final String RESULT_TEXT_KEY = "result_text";
    private boolean equalsBeenPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_calculator);

        basicTextView = findViewById(R.id.basicTextView);


        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString(RESULT_TEXT_KEY);
            basicTextView.setText(savedText);
            basicInput.append(savedText);
        }

        findViewById(R.id.basicNumZeroButton).setOnClickListener(this);
        findViewById(R.id.basicNumOneButton).setOnClickListener(this);
        findViewById(R.id.basicNumTwoButton).setOnClickListener(this);
        findViewById(R.id.basicNumThreeButton).setOnClickListener(this);
        findViewById(R.id.basicNumFourButton).setOnClickListener(this);
        findViewById(R.id.basicNumFiveButton).setOnClickListener(this);
        findViewById(R.id.basicNumSixButton).setOnClickListener(this);
        findViewById(R.id.basicNumSevenButton).setOnClickListener(this);
        findViewById(R.id.basicNumEightButton).setOnClickListener(this);
        findViewById(R.id.basicNumNineButton).setOnClickListener(this);
        findViewById(R.id.basicAdditionButton).setOnClickListener(this);
        findViewById(R.id.basicSubtractionButton).setOnClickListener(this);
        findViewById(R.id.basicMultiplicationButton).setOnClickListener(this);
        findViewById(R.id.basicDivisionButton).setOnClickListener(this);
        findViewById(R.id.basicEqualsButton).setOnClickListener(this);
        findViewById(R.id.basicFloatingPointButton).setOnClickListener(this);
        findViewById(R.id.basicBkspButton).setOnClickListener(this);
        findViewById(R.id.basicCCEButton).setOnClickListener(this);
        findViewById(R.id.basicPlusMinusButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String buttonText = button.getText().toString();
        int viewID = v.getId();

        int inputLength = basicInput.length() - 1;

        if (viewID == R.id.basicBkspButton) {
            if (basicInput.length() == 0) {
                return;
            }
            if (basicInput.charAt(0) == '-' && basicInput.length() == 1) {
                basicInput.deleteCharAt(basicInput.length() - 1);
                basicTextView.setText(basicInput);
                return;
            }
            if (!basicInput.toString().isEmpty()) {
                basicInput.deleteCharAt(basicInput.length() - 1);
                basicTextView.setText(basicInput);
                return;
            }

        } else if (viewID == R.id.basicCCEButton) {
            basicInput.setLength(0);
            basicTextView.setText("");
            return;
        } else if (viewID == R.id.basicPlusMinusButton && basicInput.length() > 0) {
            if (basicInput.charAt(0) == '-' && basicInput.length() == 1) {
                return;
            }

            int operatorIndex = -1;
            for (int i = inputLength; i >= 0; i--) {
                if (isOperator(basicInput.charAt(i))) {
                    operatorIndex = i;
                    break;
                }
            }
            String[] substrings = basicInput.toString().split("[-+*/()]");
            String lastNumber = substrings[substrings.length - 1];
            if (isOperator(basicInput.charAt(inputLength)) || Objects.equals(lastNumber, "0")) {
                return;
            }


            if (operatorIndex == -1) {
                basicInput.insert(0, "-");
            } else if (operatorIndex == 0) {
                if (basicInput.charAt(0) == '-') {
                    basicInput.deleteCharAt(0);
                }
            } else if (basicInput.charAt(operatorIndex) == '-' && basicInput.charAt(operatorIndex - 1) == '(') {
                basicInput.deleteCharAt(operatorIndex - 1);
                basicInput.deleteCharAt(operatorIndex - 1);
                basicInput.deleteCharAt(operatorIndex + lastNumber.length() - 1);
            } else if (basicInput.charAt(operatorIndex) == '+') {
                basicInput.insert(operatorIndex + 1, "(-");
                basicInput.append(')');
            } else if ((basicInput.charAt(operatorIndex + 1) == '-')) {
                basicInput.deleteCharAt(operatorIndex);
                basicInput.deleteCharAt(operatorIndex);
                basicInput.deleteCharAt(operatorIndex + lastNumber.length() - 1);
            } else {
                basicInput.insert(operatorIndex + 1, "(-");
                basicInput.append(')');
            }
        } else if (viewID == R.id.basicFloatingPointButton) {
            if (!basicInput.toString().isEmpty() &&
                    basicInput.charAt(inputLength) != ')' &&
                    !isOperator(basicInput.charAt(inputLength))) {
                String[] substrings = basicInput.toString().split("[-+*/()]");
                String lastNumber = substrings[substrings.length - 1];
                if (!lastNumber.contains(".")) {
                    basicInput.append(buttonText);
                }
            }
        } else if (validateOperators(viewID) || isDigit(viewID)) {
            if (isDigit(viewID) && equalsBeenPressed) {
                basicInput.setLength(0);
            } else {
                equalsBeenPressed = false;
            }
            if (basicInput.length() == 1) {
                if (basicInput.charAt(0) == '-' && isDigit(viewID)) {
                    basicInput.append(buttonText);
                    basicTextView.setText(basicInput.toString());
                    return;
                }
            }

            String[] substrings = basicInput.toString().split("[-+*/()]");
            String lastNumber = substrings[substrings.length - 1];

            if (isDigit(viewID) && lastNumber.equals("0") && !isOperator(basicInput.charAt(basicInput.length() - 1))) {
                basicInput.deleteCharAt(basicInput.length() - 1);
                basicInput.append(buttonText);
                basicTextView.setText(basicInput.toString());
                return;
            } else if (isDigit(viewID) && lastNumber.equals("0") && lastNumber.contains(".")) {
                basicInput.append(buttonText);
                basicTextView.setText(basicInput.toString());
                return;
            } else if (isDigit(viewID) && !lastNumber.contains(".") && lastNumber.equals("0")
                    && !isOperator(basicInput.charAt(basicInput.length() - 1))) {
                basicInput.deleteCharAt(basicInput.length() - 1);
                basicInput.append(buttonText);
                basicTextView.setText(basicInput.toString());
                return;
            } else if (validateOperators(viewID)) {
                basicInput.append(buttonText);
                basicTextView.setText(basicInput.toString());
                return;
            }

            if (isDigit(viewID) && !basicInput.toString().isEmpty()) {
                if (basicInput.charAt(inputLength) == ')') {
                    return;
                }
            }
            basicInput.append(buttonText);
        }
        if (viewID == R.id.basicEqualsButton) {
            calculateResult();
            equalsBeenPressed = true;
        } else {
            equalsBeenPressed = false;
        }
        if (viewID != R.id.basicEqualsButton) {
            basicTextView.setText(basicInput.toString());
        }
    }

    private void calculateResult() {
        try {
            Context rhino = Context.enter();
            rhino.setOptimizationLevel(-1);

            Scriptable scope = rhino.initStandardObjects();


            if (isOperator(basicInput.charAt(basicInput.length() - 1))) {
                basicInput.deleteCharAt(basicInput.length() - 1);
            }
            if (basicInput.toString().contains("/0") && !basicInput.toString().contains("/0.")) {
                Toast.makeText(this, "Can't divide by zero", Toast.LENGTH_SHORT).show();
                return;
            }
            Object result = rhino.evaluateString(scope, basicInput.toString(), "JavaScript", 1, null);
            if (result.toString().equals("NaN") ||
                    result.toString().equals("-Infinity") ||
                    result.toString().equals("Infinity")) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                return;
            }
            basicInput.setLength(0);
            if (result.toString().charAt(result.toString().length() - 2) == '.' &&
                    result.toString().charAt(result.toString().length() - 1) == '0') {
                String new_result = result.toString();
                new_result = new_result.substring(0, new_result.length() - 2);
                basicInput.append(new_result);
                basicTextView.setText(new_result);
            } else {
                basicInput.append(result);
                basicTextView.setText(result.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateOperators(int buttonID) {
        String inputText = basicInput.toString();

        if (inputText.isEmpty()) {
            return false;
        } else if (!isOperator(inputText.charAt(inputText.length() - 1)) && !isFloatingpoint(inputText.charAt(inputText.length() - 1))) {
            if (buttonID == R.id.basicAdditionButton ||
                    buttonID == R.id.basicSubtractionButton ||
                    buttonID == R.id.basicMultiplicationButton ||
                    buttonID == R.id.basicDivisionButton ||
                    buttonID == R.id.basicFloatingPointButton) {
                return true;
            }
        }
        return false;
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private boolean isFloatingpoint(char c) {
        return c == '.';
    }

    private boolean isDigit(int buttonID) {
        return buttonID == R.id.basicNumZeroButton ||
                buttonID == R.id.basicNumOneButton ||
                buttonID == R.id.basicNumTwoButton ||
                buttonID == R.id.basicNumThreeButton ||
                buttonID == R.id.basicNumFourButton ||
                buttonID == R.id.basicNumFiveButton ||
                buttonID == R.id.basicNumSixButton ||
                buttonID == R.id.basicNumSevenButton ||
                buttonID == R.id.basicNumEightButton ||
                buttonID == R.id.basicNumNineButton;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RESULT_TEXT_KEY, basicTextView.getText().toString());
    }
}