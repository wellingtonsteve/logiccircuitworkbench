/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.testing;

import sim.joinable.*;

/**
 *
 * @author Stephen
 */
public class NewClass3 {

    static Joinable a = new OutputPin(null,"") {public String toString(){return "a";}};
    static Joinable b = new Joinable() {public String toString(){return "b";}};
    static Joinable c = new Joinable() {public String toString(){return "c";}};
    static Joinable d = new Joinable() {public String toString(){return "d";}};
    static Joinable e = new OutputPin(null,"") {public String toString(){return "e";}};

    public static void printOutputSource(){
        System.out.println(a.outputSource);
        System.out.println(b.outputSource);
        System.out.println(c.outputSource);
        System.out.println(d.outputSource);
        System.out.println(e.outputSource);
        System.out.println("");
    }

    public static void main(String[] args){

        a.outputSource = (OutputPin) a;
        printOutputSource();

        Joinable.connect(a, b);
        Joinable.connect(c, b);
        printOutputSource();

        Joinable.connect(c, d);
        printOutputSource();

        Joinable.disconnect(a, b);
        printOutputSource();

        Joinable.disconnect(b, c);
        printOutputSource();

        Joinable.connect(a, b);
        printOutputSource();

        Joinable.connect(d, e);
        printOutputSource();

        Joinable.connect(b, e);
        printOutputSource();
    }
}
