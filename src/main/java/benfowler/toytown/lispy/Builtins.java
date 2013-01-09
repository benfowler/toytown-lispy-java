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

import static au.id.bjf.toylisp.Interpreter.read;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Builtins {

	private final static String ENV_CLS = Builtins.class.getName();


	static Environment getGlobalEnvironment() {
		final Environment env= new Environment(null);
		env.put("+", new Proc(strList("a", "b"), env,
				read("(.plus "+ ENV_CLS +" a b)")));
		env.put("-", new Proc(strList("a", "b"), env,
				read("(.minus "+ ENV_CLS +" a b)")));
		env.put("*", new Proc(strList("a", "b"), env,
				read("(.mul "+ ENV_CLS +" a b)")));
		env.put("/", new Proc(strList("a", "b"), env,
				read("(.div "+ ENV_CLS +" a b)")));
		env.put("%", new Proc(strList("a", "b"), env,
				read("(.mod "+ ENV_CLS +" a b)")));
		env.put("<", new Proc(strList("a", "b"), env,
				read("(.lt "+ ENV_CLS +" a b)")));
		env.put(">", new Proc(strList("a", "b"), env,
				read("(.gt "+ ENV_CLS +" a b)")));
		env.put("<=", new Proc(strList("a", "b"), env,
				read("(.lte "+ ENV_CLS +" a b)")));
		env.put(">=", new Proc(strList("a", "b"), env,
				read("(.gte "+ ENV_CLS +" a b)")));
		env.put("=", new Proc(strList("a", "b"), env,
				read("(.equal1 "+ ENV_CLS +" a b)")));
		env.put("equal?", new Proc(strList("a", "b"), env,
				read("(.equal2 "+ ENV_CLS +" a b)")));
		env.put("eq?", new Proc(strList("a", "b"), env,
				read("(.equal3 "+ ENV_CLS +" a b)")));
		env.put("not", new Proc(strList("a"), env,
				read("(.not "+ ENV_CLS +" a)")));
		env.put("length", new Proc(strList("a"), env,
				read("(.length "+ ENV_CLS +" a)")));
		env.put("cons", new Proc(strList("a", "b"), env,
				read("(.cons "+ ENV_CLS +" a b)")));
		env.put("car", new Proc(strList("a"), env,
				read("(.car "+ ENV_CLS +" a)")));
		env.put("cdr", new Proc(strList("a"), env,
				read("(.cdr "+ ENV_CLS +" a)")));
		env.put("append", new Proc(strList("a", "b"), env,
				read("(.append "+ ENV_CLS +" a b)")));
		env.put("list", new Proc(strList("a"), env,
				read("(.list "+ ENV_CLS +" a)"), true));
		env.put("list?", new Proc(strList("a", "b"), env,
				read("(.list2 "+ ENV_CLS +" a b)")));
		env.put("null?", new Proc(strList("a"), env,
				read("(.null1 "+ ENV_CLS +" a)")));
		env.put("symbol?", new Proc(strList("a"), env,
				read("(.symbol "+ ENV_CLS +" a)")));
		env.put("display", new Proc(strList("a"), env,
				read("(.display " + ENV_CLS + " a)"), true));

		return env;
	}

	//
	// Builtins exposed to interpreter
	//

    public static Double plus(final Double a, final Double b) {
    	return a + b;
    }

    public static Double minus(final Double a, final Double b) {
    	return a - b;
    }

    public static Double mul(final Double a, final Double b) {
    	return a * b;
    }

    public static Double div(final Double a, final Double b) {
    	return a / b;
    }

    public static Double mod(final Double a, final Double b) {
    	return a % b;
    }

    public static Boolean lt(final Double a, final Double b) {
    	return a < b;
    }

    public static Boolean gt(final Double a, final Double b) {
    	return a > b;
    }

    public static Boolean lte(final Double a, final Double b) {
    	return a <= b;
    }

    public static Boolean gte(final Double a, final Double b) {
    	return a >= b;
    }

    public static Boolean equal1(final Double a, final Double b) {
    	return equalObject(a, b);
    }

    public static Boolean equal2(final Double a, final Double b) {
    	return equalObject(a, b);
    }

    private static Boolean equalObject(final Object a, final Object b) {
    	if (a == null && b == null) {
			return true;
		} else if (a != null && b != null && a.equals(b)) {
			return true;
		} else {
			return false;
		}
    }

    public static Boolean equal3(final Double a, final Double b) {
    	return a == b;
    }

    public static Boolean not(final Boolean arg) {
    	return (arg != null ? !arg.booleanValue() : false);
    }

    public static Double length(final LinkedList<?> a) {
    	return new Double(a.size());
    }

    public static LinkedList<Object> cons(final LinkedList<?> a, final LinkedList<?> b) {
    	final LinkedList<Object> result = new LinkedList<Object>();
    	result.add(a);
    	if (b != null) {
			result.addAll(b);
		}
    	return result;
    }

    public static LinkedList<Object> cons(final Double a, final LinkedList<?> b) {
    	final LinkedList<Object> result = new LinkedList<Object>();
    	result.add(a);
    	if (b != null) {
			result.addAll(b);
		}
    	return result;
    }

    public static Object car(final LinkedList<?> a) {
    	return (a != null && a.size() > 0 ? a.getFirst(): null);
    }

    public static Object cdr(final LinkedList<Object> a) {
    	if (a != null && a.size() > 1) {
    		final LinkedList<Object> result = new LinkedList<Object>(a);
    		result.removeFirst();
    		return result;
    	} else {
    		return new LinkedList<Object>();
    	}
    }

    public static LinkedList<Object> append(final LinkedList<?> a, final LinkedList<?> b) {
    	final LinkedList<Object> result = new LinkedList<Object>();
    	if (a != null) {
			result.addAll(a);
		}
    	if (b != null) {
			result.addAll(b);
		}
    	return result;
    }

    public static Object list(final LinkedList<?> a) {
    	// Proc arguments are bound as all-args-into-list, simply return the
    	// list-ified actual arguments
    	return a;
    }

    public static Boolean list2(final Object a) {
    	return (a instanceof List);
    }

    public static Boolean null1(final LinkedList<?> a) {
    	return (a == null || a.size() == 0);
    }

    public static Boolean null1(final Double a) {
    	return false;
    }

    public static Boolean symbol(final Object a) {
    	return (a instanceof Symbol);
    }

    public static void display(final LinkedList<?> a) {
    	for (final Object o : a) {
    		System.out.println(o);
    	}
    }


    //
    // Static helper methods
    //

    private static List<String> strList(final String... objects) {
    	return Arrays.asList(objects);
    }

}
