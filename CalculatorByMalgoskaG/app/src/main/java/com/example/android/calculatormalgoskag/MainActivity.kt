package com.example.android.calculatormalgoskag

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.jetbrains.anko.toast
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    // 2 zmienne - w kotlinie tworzy się je przez słowo kluczowe VAR
    //Jedna zawiera pełny string z działaniem -operationList- a druga przechowuje aktualnie wbijaną liczbę lub znak -numberCache-
    var operationList: String = ""
    var numberCache: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            updateDisplay(operationList, numberCache)

    }

    //Nadpisuje te metody żeby zabezpieczyć się przed zniknięciem moich danych
    //w momencie kiedy ekran telefonu zmienia orientację (dla złośliwców = wiadomo o jaką orientacje chodzi!)
    //można sobie doczytać co się dzieje, kiedy obracamy telefon! - podpowiedź - cykl życia naszego activity
    //należy zapisać wszytskie zmienne a następnie odtworzyć je w nowym activity
    public override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("OPERATION_LIST_1",operationList)
        outState?.putString("NUMBER_CACHE_1",numberCache)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

            operationList = savedInstanceState?.getString("OPERATION_LIST_1").toString()
            numberCache = savedInstanceState?.getString("NUMBER_CACHE_1").toString()

        updateDisplay(operationList,numberCache)
    }





    // czyszczenie zmiennych
    fun clearCache() {
        numberCache = ""
        operationList = ""

    }

    //aktualizacja wyświetlacza kalkulatora
    fun updateDisplay(mainDisplayString: String, mainDisplayChar: String) {


        fullCalculationText.text = mainDisplayString
        textView.text = mainDisplayChar

    }

    //sprawdza czy w zmiennej przechowującej aktualnie wbijany znak, jest liczba czy znak działania
    //zwraca TRUE = liczba FALSE = któryś ze znaków (* + - /)
    //wykorzystuje wyrażenia regularne - można doczytać
    fun isNumeric(s: String?): Boolean {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+".toRegex())
    }


    //======================================================
    // Funkcje odpowiedzialne za kliknięcie któregoś przycisku
    //======================================================

    //Klawisz C - czyści Stringi z działaniami za pomocą metody clearCache()
    fun clearClick(view: View) {
        clearCache()
        updateDisplay("", "");
    }


    //Klawisz   =   oblicza wynik z dostępnego Stringa
    //wykorzystuje zewnętrzną klasę JAVA - MathEval - można doczytać
    //jeśli ktoś programował w javaScript będzie znał polecenie eval() - zmienia string na działanie matematyczne
    fun equalsClick(view: View?) {

        if (operationList.isEmpty()) {
            return
        } else if (isNumeric(numberCache)) {
            operationList = operationList + numberCache
        }


        val mathEval = MathEval()
        var result = mathEval.evaluate(operationList)

        //pozbywam się problemu z .0
        val test = result - result.toInt()

        if(test == 0.0)
        {
            updateDisplay("", "= " + result.toInt())
        }
        else
        {
            updateDisplay("", "= " + result)
        }

        clearCache()
    }


    // liczba dodatnia/ujemna - nie chciało mi się :P
    fun negateNumber(view: View) {

        toast("jeszcze nie dziala, ale będzie")

    }


    // funkca wywoływana kiedy zostanie kliknięty jakiś przycisk ze znakiem
    fun buttonClick(view: View) {

        val button = view as Button

        //jeśli nic nie ma to zwróć nic
        if (numberCache.isEmpty()) {
            return
        }
        //jesli są cyfry przenieś je do głównego stringa -> zmień cache na znak
        else if (isNumeric(numberCache)) {
            operationList = operationList + numberCache
            numberCache = button.text.toString()
        }
        //jeśli jest znak -> wymień go
        else {
            numberCache = button.text.toString()
        }

        //niezależnie od wszytskiego odświerzamy pola tekstowe!
        updateDisplay(operationList, numberCache)

    }

    // funkcja wywoływana kiedy zostanie kliknięty jakiś przycisk z cyfrą
    fun numberClick(view: View) {
        val button = view as Button
        val numberString = button.text;

        //jeśli w cache jest znak -> przenieś do stringa głównego i wyczyść
        if (!isNumeric(numberCache)) {
            operationList = operationList + numberCache
            numberCache = ""
        }
        //dodawaj klikane cyfry do ciągu
        numberCache = numberCache + numberString.toString()
        updateDisplay(operationList, numberCache)

    }

}