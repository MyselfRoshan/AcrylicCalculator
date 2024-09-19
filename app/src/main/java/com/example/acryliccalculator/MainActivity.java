package com.example.acryliccalculator;

import android.os.Bundle;
import android.text.Editable;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText displayInput;
    private TextView displayResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sin = findViewById(R.id.sin);
        Button cos = findViewById(R.id.cos);
        Button openBracket = findViewById(R.id.open_bracket);
        Button closeBracket = findViewById(R.id.close_bracket);

        Button ac = findViewById(R.id.clear_all);
        Button c = findViewById(R.id.clear);

        ArrayList<Button> numbers = new ArrayList<>();
        numbers.add(findViewById(R.id.zero));
        numbers.add(findViewById(R.id.one));
        numbers.add(findViewById(R.id.two));
        numbers.add(findViewById(R.id.three));
        numbers.add(findViewById(R.id.four));
        numbers.add(findViewById(R.id.five));
        numbers.add(findViewById(R.id.six));
        numbers.add(findViewById(R.id.seven));
        numbers.add(findViewById(R.id.eight));
        numbers.add(findViewById(R.id.nine));

        ArrayList<Button> operators = new ArrayList<>();
        operators.add(findViewById(R.id.add));
        operators.add(findViewById(R.id.subtract));
        operators.add(findViewById(R.id.multiply));
        operators.add(findViewById(R.id.divide));

        Button decimal = findViewById(R.id.decimal);
        decimal.setOnClickListener((e) -> insertAtCursor(decimal.getText().toString()));

        Button equalTo = findViewById(R.id.equal_to);

        equalTo.setOnClickListener((e) -> {
            String expression = displayInput.getText().toString();
            String result = evaluateExpression(expression);
            displayResult.setText(result);
        });

        displayInput = findViewById(R.id.display_input);
        displayResult = findViewById(R.id.display_result);

//        Focus and no keyboard upon focus
        displayInput.requestFocus();
        displayInput.setShowSoftInputOnFocus(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        openBracket.setOnClickListener((e) -> insertAtCursor(openBracket.getText().toString()));
        closeBracket.setOnClickListener((e) -> insertAtCursor(closeBracket.getText().toString()));
        sin.setOnClickListener((e) -> insertAtCursor("sin()", true));
        cos.setOnClickListener((e) -> insertAtCursor("cos()", true));


        c.setOnClickListener((e) -> deleteCharacterLeftOfCursor());
        ac.setOnClickListener((e) -> clearAll());


        numbers.forEach(number -> number.setOnClickListener((e) -> insertAtCursor(number.getText().toString())));
        operators.forEach(operator -> operator.setOnClickListener((e) -> insertAtCursor(operator.getText().toString())));
        // Add TextWatcher to listen for changes in the EditText
//        displayInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Evaluate the expression and update the resultTextView
//                String expression = s.toString();
//                String result = evaluateExpression(expression);
//                displayResult.setText(result);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
    }

    public void insertAtCursor(String text, boolean isTrigonometric) {
        int start = displayInput.getSelectionStart();
        int end = displayInput.getSelectionEnd();

        // Insert text at the cursor position
        Editable editable = displayInput.getText();
        editable.replace(start, end, text);
        if (isTrigonometric) {
            displayInput.setSelection(start + text.length() - 1);
        }
    }

    public void insertAtCursor(String text) {
        int start = displayInput.getSelectionStart();
        int end = displayInput.getSelectionEnd();

        // Insert text at the cursor position
        Editable editable = displayInput.getText();
        editable.replace(start, end, text);
    }

    public void clearAll() {
        displayInput.setText("");
    }

    public void deleteCharacterLeftOfCursor() {
        int cursorPosition = displayInput.getSelectionStart();
        if (cursorPosition > 0) {
            String text = displayInput.getText().toString();
            String newText = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
            displayInput.setText(newText);
            displayInput.setSelection(cursorPosition - 1);
        }
    }

    private String evaluateExpression(String expression) {
        if (expression.isEmpty()) return "0";
        try {
            Expression result = new ExpressionBuilder(expression).build();
            BigDecimal bd = BigDecimal.valueOf(result.evaluate());
            bd = bd.setScale(10, RoundingMode.HALF_UP);
            return bd.stripTrailingZeros().toPlainString();
        } catch (Exception e) {
            return "Error";
        }
    }

}