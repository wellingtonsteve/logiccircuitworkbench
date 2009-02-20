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
    public static void main(String[] args){
        Joinable a = new OutputPin(null,"") {public String toString(){return "a";}};
        Joinable b = new Joinable() {public String toString(){return "b";}};
        Joinable c = new Joinable() {public String toString(){return "c";}};
        Joinable d = new Joinable() {public String toString(){return "d";}};

        a.outputSource = (OutputPin) a;
        System.out.println("");

        System.out.println(a.outputSource);
        System.out.println(b.outputSource);
        System.out.println(c.outputSource);
        System.out.println(d.outputSource);
        System.out.println("");

        Joinable.connect(a, b);
        Joinable.connect(c, b);
        Joinable.connect(c, a);

        System.out.println(a.outputSource);
        System.out.println(b.outputSource);
        System.out.println(c.outputSource);
        System.out.println(d.outputSource);
        System.out.println("");

        Joinable.connect(c, d);

        System.out.println(a.outputSource);
        System.out.println(b.outputSource);
        System.out.println(c.outputSource);
        System.out.println(d.outputSource);
        System.out.println("");

        Joinable.disconnect(a, b);

        System.out.println(a.outputSource);
        System.out.println(b.outputSource);
        System.out.println(c.outputSource);
        System.out.println(d.outputSource);
        System.out.println("");

        Joinable.disconnect(a, c);

        System.out.println(a.outputSource);
        System.out.println(b.outputSource);
        System.out.println(c.outputSource);
        System.out.println(d.outputSource);
        System.out.println("");

        Joinable.disconnect(b, c);

        System.out.println(a.outputSource);
        System.out.println(b.outputSource);
        System.out.println(c.outputSource);
        System.out.println(d.outputSource);
        System.out.println("");

        Joinable.connect(a, b);

        System.out.println(a.outputSource);
        System.out.println(b.outputSource);
        System.out.println(c.outputSource);
        System.out.println(d.outputSource);
        System.out.println("");

        Joinable.connect(b, c);

        System.out.println(a.outputSource);
        System.out.println(b.outputSource);
        System.out.println(c.outputSource);
        System.out.println(d.outputSource);
        System.out.println("");
    }
}
