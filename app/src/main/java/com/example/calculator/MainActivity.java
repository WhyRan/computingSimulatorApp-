package com.example.calculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button buttonAnswer;
    private TextView textViewAnswerFalse;
    private TextView textViewAnswerTrue;
    private TextView textViewQuestion;

    private String numberOne;
    private String numberTwo;
    private int result;
    private char operator;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(savedInstanceState != null){
            numberOne = savedInstanceState.getString("numberOne");
            numberTwo = savedInstanceState.getString("numberTwo");
        }

        initView();

        if(numberOne == null) {
            randomOperators();
            randomNumbers();
        }
        result();

            textViewQuestion.setText(numberOne + " " + operator + " " + numberTwo + " = ");

            buttonAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = editText.getText().toString();

                    if (buttonAnswer.getText().equals("Next example")) {
                        // Генерируем новый пример
                        randomOperators();
                        randomNumbers();
                        result();
                        textViewQuestion.setText(numberOne + " " + operator + " " + numberTwo + " = ");
                        editText.setText("");
                        textViewAnswerTrue.setVisibility(View.GONE);
                        buttonAnswer.setText("Suggest an answer");
                    }else if (text.equals(Integer.toString(result))) {
                        textViewAnswerTrue.setVisibility(View.VISIBLE);
                        textViewAnswerFalse.setVisibility(View.GONE);
                        buttonAnswer.setText("Next example");
                    } else {
                        textViewAnswerTrue.setVisibility(View.GONE);
                        textViewAnswerFalse.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("numberOne" , numberOne);
        outState.putString("numberTwo" , numberTwo);
    }

    private void initView(){
        editText = findViewById(R.id.editTextNumber);
        buttonAnswer = findViewById(R.id.buttonAnswer);
        textViewAnswerFalse = findViewById(R.id.textViewAnswerFalse);
        textViewAnswerTrue = findViewById(R.id.textViewAnswerTrue);
        textViewQuestion = findViewById(R.id.textViewQuestion);
    }

    private void randomOperators(){
        char[] operator = {43, 45, 42, 47};
        Random random = new Random();
        this.operator = operator[random.nextInt(4)];
    }

    private void randomNumbers(){
        int randomNumberOne = 0;
        int randomNumberTwo = 0;

        Random random = new Random();

        switch (operator) {
            case 43:
                do {
                    randomNumberOne = random.nextInt(100);
                    randomNumberTwo = random.nextInt(100);
                }while (randomNumberOne != 0 && randomNumberTwo !=0);
                break;
            case 45:
                do {
                randomNumberOne = random.nextInt(100);
                randomNumberTwo = random.nextInt(100);
            } while (randomNumberOne <= randomNumberTwo);
            break;
            case 42:
                    randomNumberOne = random.nextInt(8) + 2;
                    randomNumberTwo = random.nextInt(8) + 2;
                break;
            case 47:
                int divider = random.nextInt(9) + 2;
                int result = random.nextInt(9) + 2;

                randomNumberOne = divider * result;
                randomNumberTwo = divider;
                break;
        }

        numberOne = Integer.toString(randomNumberOne);
        numberTwo = Integer.toString(randomNumberTwo);
    }

    private void result(){
        switch (operator){
            case 43:
                result = Integer.parseInt(numberOne) + Integer.parseInt(numberTwo);
                break;
            case 45:
                result = Integer.parseInt(numberOne) - Integer.parseInt(numberTwo);
                break;
            case 42:
                result = Integer.parseInt(numberOne) * Integer.parseInt(numberTwo);
                break;
            case 47:
                result = Integer.parseInt(numberOne) / Integer.parseInt(numberTwo);
        }
    }

}
