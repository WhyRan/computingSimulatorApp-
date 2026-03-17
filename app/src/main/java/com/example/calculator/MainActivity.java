package com.example.calculator;


import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
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

    //Иницилизация активных элементов экрана
    private EditText editText;
    private Button buttonAnswer;
    private Button buttonDelete;
    private Button buttonRestart;
    private TextView textViewQuestion;
    //Переменные для смены цвета кнопки ответа
    private boolean isGreen;
    //Массив кнопок для построения общего слушателя
    private final Button[] buttons = new Button[10];
    //Числа отображаемые на экране в виде примера
    private String numberOne;
    private String numberTwo;
    //Переменая для ввода ответа
    private String userNumber = "";
    //переменные для составления и проверки примера
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
        //Отключение смены ориентации
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initView();

        //Сохранение состояния Activity при изменении жизненного цикла
        if (savedInstanceState != null) {
            numberOne = savedInstanceState.getString("numberOne");
            numberTwo = savedInstanceState.getString("numberTwo");
            operator = savedInstanceState.getChar("operator");

            result();

            textViewQuestion.setText(numberOne + " " + operator + " " + numberTwo + " = ?");
        }

        if (numberOne == null) {
            generateExample();
        }
        //Реализация перезапуска примера
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateExample();
                defaultState();
            }
        });
        //Реализация удаления ввода
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userNumber != null && !userNumber.isEmpty()) {
                    userNumber = userNumber.substring(0, userNumber.length() - 1);
                    editText.setText(userNumber);
                }
            }
        });
        //Реализация кнопки правильного ответа
        buttonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (isGreen) {
                    // Генерируем новый пример
                    generateExample();
                    defaultState();
                } else if (text.equals(Integer.toString(result))) {
                    buttonAnswer.setBackgroundColor(getColor(R.color.green));
                    isGreen = true;
                    deleteVar();
                    editText.setHint("The answer is correct");
                    new Handler().postDelayed(() -> buttonAnswer.performClick(), 1000);
                } else {
                    buttonAnswer.setBackgroundColor(getColor(R.color.red));
                    editText.setHint("Incorrect, please try again");
                    deleteVar();
                }
            }
        });
        //Реализация кликера на цифровую клавиатуру
        int[] buttonsId = {
                R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn0
        };

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < buttonsId.length; i++) {
                    if (v.getId() == buttonsId[i]) {
                        onButtonClicked(i);
                        break;
                    }
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            buttons[i] = findViewById(buttonsId[i]);
            buttons[i].setOnClickListener(listener);
        }
    }

    /**
     * Метод навешивает слушатель кликов на цифровую клавиатуру
     * @param i - индекс массива
     */
    private void onButtonClicked(int i) {
            userNumber += buttons[i].getText().toString();
            editText.setText(userNumber);
            buttonAnswer.setBackgroundColor(getColor(R.color.gray));
            editText.setHint("input answer");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("numberOne", numberOne);
        outState.putString("numberTwo", numberTwo);
        outState.putChar("operator", operator);
    }
    /**
    * Метод объявления активных элементов экрана
     * editText - поле ввода для пользователя
     * buttonAnswer - кнопка проверки ответа для пользователя
     * buttonDelete - кнопка удаления ввода
     * buttonRestart - кнопка ручной генерации примера
     */
    private void initView() {
        editText = findViewById(R.id.editTextNumber);
        buttonAnswer = findViewById(R.id.buttonAnswer);
        buttonDelete = findViewById(R.id.btnDelete);
        buttonRestart = findViewById(R.id.buttonRestart);
        textViewQuestion = findViewById(R.id.textViewQuestion);
    }

    /**
     * Метод генерации алгорифмитического оператора
     * Массив основан на кодировке ASCII
     */
    private void randomOperators() {
        char[] operator = {43, 45, 42, 47};
        Random random = new Random();
        this.operator = operator[random.nextInt(4)];
    }

    /**
     * Метод генерирующий числа для примеров
     */
    private void randomNumbers() {
        int randomNumberOne = 0;
        int randomNumberTwo = 0;

        Random random = new Random();

        switch (operator) {
            case 43:
                randomNumberOne = random.nextInt(98) + 2;
                randomNumberTwo = random.nextInt(98) + 2;
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

    /**
     * Метод считающий ответ
     */
    private void result() {
        switch (operator) {
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

    /**
     * Метод сбрасывает значения переменных для корректного ввода
     */
    private void deleteVar(){
        userNumber = "";
        editText.setText("");
    }

    /**
     * Метод обобщает все действия связанные с генератором примеров
     */
    private void generateExample(){
        randomOperators();
        randomNumbers();
        result();
        textViewQuestion.setText(numberOne + " " + operator + " " + numberTwo + " = ?");

    }

    /**
     * Метод сбрасывает состояние экрана до начального
     */
    private void defaultState(){
        buttonAnswer.setBackgroundColor(getColor(R.color.gray));
        isGreen = false;
        deleteVar();
        editText.setHint("input answer");
    }
}
