/*
 *  Toytown -- a reimplementation of Peter Norvig's lis.py interpreter for
 *  Python.
 *
 *  Copyright (C) 2012-2013  Benjamin J. Fowler.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package au.id.bjf.toylisp;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestLispyTestSuite {

	private static Object[][] LISPY_TESTS = {
		new Object[] {"pn1", "(quote (testing 1 (2.0) -3.14e159))", list(sym("testing"), 1., list(2.), -3.14e159)},
		new Object[] {"pn2", "(+ 2 2)", 4. },
		new Object[] {"pn3", "(+ (* 2 100) (* 1 10))", 210.},
		new Object[] {"pn4", "(if (> 6 5) (+ 1 1) (+ 2 2))", 2.},
		new Object[] {"pn5", "(if (< 6 5) (+ 1 1) (+ 2 2))", 4.},
		new Object[] {"pn6", "(define x 3)", null},
		new Object[] {"pn6a", "x", 3.},
		new Object[] {"pn6b", "(+ x x)", 6.},
		new Object[] {"pn6c", "(begin (define x 1) (set! x (+ x 1)) (+ x 1))", 3.},
		new Object[] {"pn7", "((lambda (x) (+ x x)) 5)", 10.},
		new Object[] {"pn8", "(define twice (lambda (x) (* 2 x)))", null},
		new Object[] {"pn8a", "(twice 5)", 10.},
		new Object[] {"pn9", "(define compose (lambda (f g) (lambda (x) (f (g x)))))", null},
		new Object[] {"pn10", "((compose list twice) 5)", list(10.)},
		new Object[] {"pn11", "(define repeat (lambda (f) (compose f f)))", null},
		new Object[] {"pn12", "((repeat twice) 5)", 20.},
		new Object[] {"pn12a", "((repeat (repeat twice)) 5)", 80.},
		new Object[] {"pn13", "(define fact (lambda (n) (if (<= n 1) 1 (* n (fact (- n 1))))))", null},
		new Object[] {"pn14", "(fact 3)", 6.},
		new Object[] {"pn15", "(fact 50)", 30414093201713378043612608166064768844377641568960512000000000000.},
		new Object[] {"pn16", "(define abs (lambda (n) ((if (> n 0) + -) 0 n)))", null},
		new Object[] {"pn17", "(list (abs -3) (abs 0) (abs 3))", list (3., 0., 3.)},
		new Object[] {"pn18",
				"(define combine (lambda (f) " +
				  "(lambda (x y) " +
				    "(if (null? x) (quote ()) " +
				        "(f (list (car x) (car y)) " +
				        "((combine f) (cdr x) (cdr y)))))))", null},

		new Object[] {"pn19", "(define zip (combine cons))", null},
		new Object[] {"pn20", "(zip (list 1 2 3 4) (list 5 6 7 8))",
				list ( list(1., 5.), list(2., 6.), list(3., 7.), list(4., 8.))},
		new Object[] {"pn21",
				"(define riff-shuffle (lambda (deck) (begin " +
						"(define take (lambda (n seq) (if (<= n 0) (quote ()) (cons (car seq) (take (- n 1) (cdr seq)))))) " +
						"(define drop (lambda (n seq) (if (<= n 0) seq (drop (- n 1) (cdr seq)))))" +
						"(define mid (lambda (seq) (/ (length seq) 2)))" +
						"((combine append) (take (mid deck) deck) (drop (mid deck) deck)))))", null},
		new Object[] {"pn22", "(riff-shuffle (list 1 2 3 4 5 6 7 8))", list (1., 5., 2., 6., 3., 7., 4., 8.)},

		new Object[] {"pn23", "((repeat riff-shuffle) (list 1 2 3 4 5 6 7 8))", list(1., 3., 5., 7., 2., 4., 6., 8.)},
		new Object[] {"pn24", "(riff-shuffle (riff-shuffle (riff-shuffle (list 1 2 3 4 5 6 7 8))))", list(1., 2., 3., 4., 5., 6., 7., 8.)},
	};

    @Parameters(name="testName={0}")
    public static Collection<Object[]> data() {
    	return Arrays.asList(LISPY_TESTS);
    }

    //
    // State for individual test case
    //

    private final String testName;
    private final String input;
    private final Object expectedOutput;


    /**
     * Test environment.  It's initialized once, then 'threaded' through the
     * test cases (since later tests depend on the environment being in a
     * certain state beforehand)
     */
    private static Environment testEnv;

    @Before
	public void setup() {
    	if (testEnv == null) {
    		testEnv = Interpreter.getGlobalEnvironment();
    		testEnv.put("pi", 3.14);
    	}
	}

	//
	// Instance methods
	//

    public TestLispyTestSuite(final String testName, final String input, final Object expectedOutput) {
    	this.testName = testName;
    	this.input = input;
    	this.expectedOutput = expectedOutput;
    }

    @Test
	public void testEval() throws LispException {
		assertEquals(testName, expectedOutput,
				Interpreter.eval(Interpreter.read(input), testEnv));
    }

    //
    // Static helper methods
    //

    private static List<Object> list(final Object... objects) {
    	return Arrays.asList(objects);
    }

	private static Symbol sym(final String lexeme) {
		return new Symbol(lexeme);
	}

}

