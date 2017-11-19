package com.quizmeapi.adaptiveweb.variables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GlobalStaticVariables {
    public static final Set<String> topics = new HashSet<>(
            Arrays.asList("Classes", "Constructor", "Java", "Variables", "Loops", "Arithmetics", "Method", "Strings",
                    "Arrays", "Control", "Operator", "2D_Arrays", "DataType", "Primitive Data Type", "Objects",
                    "InputOutput", "Decisions", "Expression"));

    public static final ArrayList<Integer> initialQuiz = new ArrayList<>(
            Arrays.asList(6, 7, 8, 9, 11, 18, 21, 24, 25, 32, 50, 55, 56, 63, 67)
    );
    public static final int quizQuestionCount = 15;
}
