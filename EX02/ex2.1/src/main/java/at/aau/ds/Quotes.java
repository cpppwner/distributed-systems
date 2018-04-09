package at.aau.ds;

import java.util.Random;

public class Quotes {

    private static final String[] QUOTES = new String[] {
            "\"There are two ways of constructing a software design. One way is to make it so simple that there are " +
                    " obviously no deficiencies. And the other way is to make it so complicated that there are no " +
                    "obvious deficiencies.\" - C.A.R. Hoare",
            "\"The trouble with programmers is that you can never tell what a programmer is doing until it’s too " +
                    "late.\" Seymour Cray",
            "\"People think that computer science is the art of geniuses but the actual reality is the opposite, " +
                    "just many people doing things that build on each other, like a wall of mini stones.\"" +
                    " - Donald Knuth",
            "\"Debugging is twice as hard as writing the code in the first place. Therefore, if you write the code " +
                    "as cleverly as possible, you are, by definition, not smart enough to debug it.\"" +
                    " - Brian W. Kernighan.",
            "\"Measuring programming progress by lines of code is like measuring aircraft building progress " +
                    "by weight.\" - Bill Gates",
            "\"Beware of bugs in the above code; I have only proved it correct, not tried it.\"" +
                    " - Donald E. Knuth.",
            "\"C is quirky, flawed, and an enormous success.\" - Dennis M. Ritchie.",
            "\"The use of COBOL cripples the mind; its teaching should therefore be regarded as a criminal offense.\"" +
                    " - E.W. Dijkstra",
            "\"I have always wished for my computer to be as easy to use as my telephone; my wish has come true " +
                    "because I can no longer figure out how to use my telephone.\"" +
                    " - Bjarne Stroustrup",
            "\"Programming today is a race between software engineers striving to build bigger and better " +
                    "idiot-proof programs, and the universe trying to build bigger and better idiots. " +
                    "So far, the universe is winning.\"" +
                    " - Rick Cook"
    };

    static String getRandomQuote() {
        return QUOTES[new Random().nextInt(QUOTES.length)];
    }
}
