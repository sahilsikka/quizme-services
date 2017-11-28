package com.quizmeapi.adaptiveweb.variables;

import java.util.*;

public class GlobalStaticVariables {
    public static final Set<String> topics = new HashSet<>(
           // Arrays.asList("Classes", "Constructor", "Java", "Variables", "Loops", "Arithmetics", "Method", "Strings",
             //       "Arrays", "Control", "Operator", "2D_Arrays", "DataType", "Primitive Data Type", "Objects",
               //     "InputOutput", "Decisions", "Expression"));
            Arrays.asList("Arrays", "Operators", "Strings", "Variables", "Methods"));

    public static final ArrayList<Integer> initialQuiz = new ArrayList<>(
            //Arrays.asList(6, 7, 8, 9, 11, 18, 21, 24, 25, 32, 50, 55, 56, 63, 67)
            Arrays.asList(9 , 29 , 48 , 71 , 88 , 18 , 77 , 53 , 73, 97)
    );
   // public static final int quizQuestionCount = 15;
   public static final int quizQuestionCount = 10;

   public static final HashMap<String, HashMap<String , String>> nextLevel = new HashMap<String, HashMap<String, String>>(){

       {
           put("Easy" , new HashMap<String, String>()
                   {
                           {
                               put((0.0 +"/"+0.33), "Easy");
                               put((0.34+ "/" +0.66), "Easy-Medium");
                               put((0.67 + "/" + 0.3), "Medium");
                           }
                   }
           );

           put("Easy-Medium" , new HashMap<String, String>()
                   {
                       {
                           put((0.0 +"/"+0.25), "Easy");
                           put((0.26+ "/" +0.50), "Easy-Medium");
                           put((0.51 + "/" + 0.75), "Medium");
                           put((0.76 + "/" + 1.00), "Medium-Hard");
                       }
                   }
           );

           put("Medium" , new HashMap<String, String>()
                   {
                       {
                           put((0.0 +"/"+0.20), "Easy");
                           put((0.21+ "/" +0.40), "Easy-Medium");
                           put((0.41 + "/" + 0.60), "Medium");
                           put((0.61 + "/" + 0.80), "Medium-Hard");
                           put((0.81 + "/" + 1.80), "Hard");
                       }
                   }
           );

           put("Medium-Hard" , new HashMap<String, String>()
                   {
                       {
                           put((0.0 +"/"+0.25), "Easy-Medium");
                           put((0.26+ "/" +0.50), "Medium");
                           put((0.51 + "/" + 0.75), "Medium-Hard");
                           put((0.76 + "/" + 1.00), "Hard");
                       }
                   }
           );
           put("Hard" , new HashMap<String, String>()
                   {
                       {
                           put((0.0 +"/"+0.33), "Medium");
                           put((0.34+ "/" +0.66), "Medium-Hard");
                           put((0.67 + "/" + 0.3), "Hard");
                       }
                   }
           );


       }

   };




}


