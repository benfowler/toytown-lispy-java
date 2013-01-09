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

import static au.id.bjf.toylisp.SpecialForm.BEGIN;
import static au.id.bjf.toylisp.SpecialForm.IF;
import static au.id.bjf.toylisp.SpecialForm.QUOTE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestEval {

	private static Object[][] EVAL_TESTS = {
		// simple stuff
		new Object[] {"empty list", list(), list()},
		new Object[] {"double", 3.7, 3.7 },
		new Object[] {"variable ref", sym("pi"), 3.14 },
		new Object[] {"quote 0", list ( QUOTE, list() ), list() },
		new Object[] {"quote 1", list ( QUOTE, 1. ), 1. },
		new Object[] {"quote 2", list ( QUOTE, 1., 2. ),
				new EvalException(Errors.INVALID_NUMBER_OF_ARGUMENTS,
						1, 1, 2) },
		new Object[] {"quote 3", list ( QUOTE, list(1.) ), list(1.) },
		new Object[] {"if too few args", list ( IF ),
				new EvalException(Errors.INVALID_NUMBER_OF_ARGUMENTS,
						2, 3, 1) },
		new Object[] {"if true; no alt", list ( IF, true, 3. ), 3. },
		new Object[] {"if true; w/ alt", list ( IF, true, 4., 5. ), 4. },
		new Object[] {"if false; w/ alt", list ( IF, false, 4., 5. ), 5. },
		new Object[] {"if pi (true)", list ( IF, sym("pi"), 4. ),
				new EvalException(Errors.BAD_IF_CONDITION, "pi")},
		new Object[] {"begin 1", list ( BEGIN, list ( sym("+"), 1., 1.)), 2. },
		new Object[] {"begin 2", list ( BEGIN,
				list ( sym("+"), 1., 1.), list ( sym("+"), 2., 3. ) ), 5. }
	};

    @Parameters(name="testName={0}")
    public static Collection<Object[]> data() {
    	return Arrays.asList(EVAL_TESTS);
    }

    //
    // State for individual test case
    //

    private final String testName;
    private final Object input;
    private final Object expectedOutput;

    /**
     * Test environment.  It's initialised once, then 'threaded' through the
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

    public TestEval(final String testName, final Object input,
    		final Object expectedOutput) {
    	this.testName = testName;
    	this.input = input;
    	this.expectedOutput = expectedOutput;
    }

    @Test
	public void testEval() throws LispException {

    	if (expectedOutput instanceof Throwable) {
    		try {
    			Interpreter.eval(input, testEnv);
    			fail("Expected to catch (but didn't get) a " +
    					testEnv.getClass().getName());
    		} catch (Throwable t) {
    			if (!(t.getClass().equals(expectedOutput.getClass()))) {
    				fail("Expected to catch a " +
    					expectedOutput.getClass().getName() + " but got a " +
    					t.getClass().getName());
    			}
    		}
    	} else {
    		assertEquals(testName, expectedOutput,
    				Interpreter.eval(input, testEnv));
    	}
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

