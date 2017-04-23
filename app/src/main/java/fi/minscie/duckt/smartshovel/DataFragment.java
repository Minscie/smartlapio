package fi.minscie.duckt.smartshovel;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;


public class DataFragment extends android.app.Fragment {

    private TextView textView;
    private TextView averTemp;
    private TextView countTextView;
    private TextView lastWeightTextView;
    private TextView totalWeightTextView;
    private TextView averageWeightTextView;
    private TextView clockTextView;
    private TextView messageTextView;

    int top_temp = 0;
    float average_temp = 0;
    float total_temp = 0;
    int count = 0;
    int status = 0;
    int old_status = 0;
    float weight = 0;
    float total_weight = 0;
    float average_weight = 0;
    String clock;
    int hh = 0;
    int min = 0;
    int sec = 0;
    int ms = 0;
    private String slaveName;
    int trigger_sec = 99;

    Button startButton;
    Button restartButton;
    Button stopButton;
    Button pauseButton;
    Button historyButton;

    String jsonStr;

    ArrayList<String> temp_array = new ArrayList<>();
    ArrayList<String> count_array = new ArrayList<>();
    ArrayList<String> weight_array = new ArrayList<>();
    ArrayList<String> time_array = new ArrayList<>();


    private Thread spinningThread;

    // Is there a coin to be clicked or not
    boolean coin = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        slaveName = getArguments().getString("SlaveName");
        Log.d("Slave name: ",slaveName);

        //Inflate view with the correct layout
        View dataView = inflater.inflate(R.layout.fragment_data, container, false);

        textView = (TextView) dataView.findViewById(R.id.temperatureValueTextView);
        averTemp = (TextView) dataView.findViewById(R.id.averageTemperatureTextView);
        countTextView = (TextView) dataView.findViewById(R.id.countValueTextView);
        lastWeightTextView = (TextView) dataView.findViewById(R.id.lastWeightValueTextView);
        totalWeightTextView = (TextView) dataView.findViewById(R.id.weightValueTextView);
        averageWeightTextView = (TextView) dataView.findViewById(R.id.averageWeightValueTextView);
        clockTextView = (TextView) dataView.findViewById(R.id.timerTextView);
        messageTextView = (TextView) dataView.findViewById(R.id.messageTextView);

        messageTextView.setText("Greetings "+slaveName);
        textView.setText(Integer.toString(top_temp));

        startButton = (Button) dataView.findViewById(R.id.startTracking);
        restartButton = (Button) dataView.findViewById(R.id.restartTracking);
        stopButton = (Button) dataView.findViewById(R.id.stopTracking);
        pauseButton = (Button) dataView.findViewById(R.id.pauseTracking);
        historyButton = (Button) dataView.findViewById(R.id.historyButton);

        startButton.setOnClickListener(new View.OnClickListener() { // Listen if the coin's imageview is clicked
            @Override
            public void onClick(View v) {
                    coin = true;
                    spinningThread.start();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() { // Listen if the coin's imageview is clicked
            @Override
            public void onClick(View v) {
                    coin = false;

                    top_temp = 0;
                    average_temp = 0;
                    total_temp = 0;
                    count = 0;
                    status = 0;
                    old_status = 0;
                    weight = 0;
                    total_weight = 0;
                    average_weight = 0;
                    hh = 0;
                    min = 0;
                    sec = 0;
                    ms = 0;
                    clock = String.format(Locale.ENGLISH,"%02d:%02d:%02d", hh, min, sec);

                textView.setText(Integer.toString(top_temp));
                averTemp.setText(String.format("%.1f", average_temp));
                countTextView.setText(Integer.toString(count));
                lastWeightTextView.setText(String.format("%.1f", weight));
                totalWeightTextView.setText(String.format("%.1f", total_weight));
                averageWeightTextView.setText(String.format("%.1f", average_weight));
                clockTextView.setText(clock);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() { // Listen if the coin's imageview is clicked
            @Override
            public void onClick(View v) {
                if(coin == true) {
                    coin = false;
                }
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() { // Listen if the coin's imageview is clicked
            @Override
            public void onClick(View v) {
                if(coin == true) {
                    coin = false;
                }

                Intent historyActivityintent = new Intent(getActivity(), HistoryActivity.class);
                Bundle b = new Bundle();
                b.putString("SlaveName", slaveName); //Your id
                historyActivityintent.putExtras(b); //Put your id to your next Intent
                startActivity(historyActivityintent);

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() { // Listen if the coin's imageview is clicked
            @Override
            public void onClick(View v) {
                if(coin == true) {
                    coin = false;
                    try {
                        temp_array.clear();
                        weight_array.clear();
                        count_array.clear();
                        time_array.clear();

                        temp_array.add(Float.toString(average_temp));
                        weight_array.add(Float.toString(total_weight));
                        count_array.add(Integer.toString(count));
                        time_array.add(clock);
                        Log.d("Log", "Array timestamp:" + time_array);
                        Log.d("Log", "Array av temp: " + temp_array);
                        Log.d("Log", "Array total weight: " + weight_array);
                        Log.d("Log", "Array count: " + count_array);


                        JSONObject workObj = new JSONObject();

                        JSONArray workArray = new JSONArray();


                        try {
                            for (int i = 0; i < count_array.size(); i++) {
                                JSONObject WorkValues = new JSONObject();

                                WorkValues.put("time", time_array.get(i));
                                workArray.put(i, WorkValues);
                                WorkValues.put("temp", temp_array.get(i));
                                workArray.put(i, WorkValues);
                                WorkValues.put("weight", weight_array.get(i));
                                workArray.put(i, WorkValues);
                                WorkValues.put("count", count_array.get(i));
                                workArray.put(i, WorkValues);
                            }

                            workObj.put("name", slaveName);
                            workObj.put("description", "Finished");
                            workObj.put("sensorStatus", "1");
                            workObj.put("readings", workArray);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        jsonStr = workObj.toString();
                        Log.d("json: ", jsonStr);

                        Runnable postJson = new PostJson(jsonStr);
                        new Thread(postJson).start();

                    } catch (Exception e) {
                        Log.e("Log", "Error: " + e);
                    }
                }
            }
        });





        spinningThread = new Thread() {  // Purpose of this thread is to spin the coin randomly between 30sec and 2sec if the user is not doing anything
            @Override                       // Just an extra effect to make it more lively
            public void run() {
                try {
                    while (coin == true) {  // Run while loop if there is a coin
                        synchronized (this) {
                            wait(100);

                            if(getActivity() == null) // Check if activity is no longer visible. http://stackoverflow.com/questions/23825549/nullpointerexception-on-getactivity-runonuithreadnew-runnable
                                return;

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        status = BluetoothConnectionService.getStatus();
                                    } catch (Exception e){
                                        Log.e("Log", "Invalid status value");
                                    }
                                    if(status != old_status && status == 1) {
                                        count++;
                                        try {
                                            top_temp = BluetoothConnectionService.getTop_Temp();
                                            total_temp += top_temp;
                                            average_temp = total_temp / count;
                                        }
                                        catch (Exception e){
                                            Log.e("Log", "Invalid temp value, skipped!");
                                        }
                                        try {
                                            weight = BluetoothConnectionService.getWeight();
                                            total_weight += weight;
                                            average_weight = (float) total_weight / count;
                                        }
                                        catch (Exception e){
                                            Log.e("Log", "Invalid weight value, skipped!");
                                        }



                                        old_status = status;



                                    }
                                    else if(status != old_status && status == 0){
                                        old_status = status;
                                    }

                                    ms++;

                                    if (ms == 10) {
                                        ms = 0;
                                        sec++;
                                    }
                                    if (sec == 60) {
                                        sec = 0;
                                        min++;
                                    }
                                    if (min == 60) {
                                        min = 0;
                                        hh++;
                                    }


                                    if(sec == 12 && average_weight < 2){
                                        trigger_sec = 99;
                                        messageTextView.setText(slaveName+", You're useless");
                                    }

                                    else if(sec == 25 && average_weight < 2){
                                        trigger_sec = 99;
                                        messageTextView.setText(slaveName+", Can you even lift?!");
                                    }

                                    if(average_temp > 18 && sec == 25 && average_weight > 2){
                                        trigger_sec = 99;
                                        messageTextView.setText(slaveName+", You can't fool me..");
                                    }

                                    else if(average_temp < 2 && sec == 25 && average_weight > 2) {
                                        trigger_sec = 99;
                                        messageTextView.setText(slaveName + ", You're doing OK!");
                                    }

                                    if(average_temp > 18 && sec == 29 && average_weight < 2){
                                        trigger_sec = 99;
                                        messageTextView.setText(slaveName+", I see you..!");
                                    }

                                    if(average_temp > 18 && sec == 35 && average_weight < 2){
                                        trigger_sec = 99;
                                        messageTextView.setText("You should be working outside!");
                                    }

                                    if(average_temp > 18 && sec == 45 && average_weight < 2){
                                        trigger_sec = 99;
                                        messageTextView.setText("That's hot weather you're having!");
                                    }

                                    if(average_temp > 18 && sec == 55 && average_weight < 2) {
                                        trigger_sec = 99;
                                        messageTextView.setText("Very suspicious temperature...");
                                    }



                                    clock = String.format(Locale.ENGLISH,"%02d:%02d:%02d", hh, min, sec);


                                    textView.setText(Integer.toString(top_temp));
                                    averTemp.setText(String.format("%.1f", average_temp));
                                    countTextView.setText(Integer.toString(count));
                                    lastWeightTextView.setText(String.format("%.1f", weight));
                                    totalWeightTextView.setText(String.format("%.1f", total_weight));
                                    averageWeightTextView.setText(String.format("%.1f", average_weight));
                                    clockTextView.setText(clock);
                                }
                            });

                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ;
        }; // spinningThread end

        return dataView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}





