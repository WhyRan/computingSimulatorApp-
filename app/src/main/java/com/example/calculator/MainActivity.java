package com.example.calculator;

import android.annotation.SuppressLint;
import android.os.Build;
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

    private String numberOne;
    private String numberTwo;
    private int result;

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

        EditText editText = findViewById(R.id.editTextNumber);
        Button buttonAnswer = findViewById(R.id.buttonAnswer);
        TextView textViewAnswerFalse = findViewById(R.id.textViewAnswerFalse);
        TextView textViewAnswerTrue = findViewById(R.id.textViewAnswerTrue);
        TextView textViewQuestion = findViewById(R.id.textViewQuestion);
        if(numberOne == null) {
            randomNumbers();
        }
        result();

            textViewQuestion.setText(numberOne + " + " + numberTwo + " = ");

            buttonAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = editText.getText().toString();

                    if (buttonAnswer.getText().equals("Next example")) {
                        // Генерируем новый пример
                        randomNumbers();
                        result();
                        textViewQuestion.setText(numberOne + " + " + numberTwo + " = ");
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

    private void randomNumbers(){
        int randomNumberOne;
        int randomNumberTwo;

        Random random = new Random();
        randomNumberOne = random.nextInt(100);
        randomNumberTwo = random.nextInt(100);

        numberOne = Integer.toString(randomNumberOne);
        numberTwo = Integer.toString(randomNumberTwo);
    }

    private void result(){
        result = Integer.parseInt(numberOne) + Integer.parseInt(numberTwo);
    }

    private void sumNumbers(){

    }
}
