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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Implements a simple toy Lisp interpreter.  Inspired by Peter Norvig's
 * lis.py implementation.
 */
public class Interpreter {

	public static Environment getGlobalEnvironment() {
		return Builtins.getGlobalEnvironment();
	}

	public static Object read(final String program) throws EvalException {
		return parse(program);
	}

	public static Object parse(final String program) throws EvalException {
		final Lexer lexer = new Lexer(program);
		Object result = null;
		if (lexer.peekToken().equals("(")) {
			result = sExpression(lexer);
		} else {
			result = lexer.consumeToken();
		}

		if (lexer.hasMoreTokens()) {
			throw new ParseException(Errors.TOKEN_AFTER_END_OF_PROGRAM);
		}

		return result;
	}

	public static Object eval(final Object arg) throws EvalException {
		return eval(arg, getGlobalEnvironment());
	}

	public static Object eval(final Object arg, final Environment env)
	throws EvalException {
		if (arg instanceof Double || arg instanceof Boolean) {
			// literal
			return arg;
		} else if (arg instanceof Symbol) {
			// symbol
			final Object resolved = env.get(arg.toString());
			if (resolved == null) {
				throw new EvalException(Errors.SYMBOL_NOT_FOUND,
						arg.toString());
			}
			return resolved;
		} else if (arg instanceof List) {
			List<?> args = (List<?>)arg;
			if (args.size() == 0) {
				return args;
			}

			if (args.get(0) instanceof Dot) {
				return dot(args, env);
			} else if (args.get(0) instanceof SpecialForm) {
				final SpecialForm sf = (SpecialForm)args.get(0);
				switch(sf) {
				case QUOTE:
					return quote(args);
				case IF:
					return if_(args, env);
				case BEGIN:
					return begin(args, env);
				case SET_:
					return set_(args, env);
				case DEFINE:
					return define(args, env);
				case LAMBDA:
					return lambda(args, env);
				default:
					throw new EvalException(Errors.INTERNAL_ERROR,
							String.format("Unexpected special form: %s",
									args.get(0).toString()));
				}
			} else {
				return proc(args, env);
			}

		} else {
			// bad input
			throw new EvalException(Errors.CANNOT_EVAL,
					arg.getClass().getName(), arg.toString());
		}
	}

	private static List<Object> sExpression(final Lexer lexer)
	throws EvalException {
		List<Object> result = new LinkedList<Object>();
		lexer.expect("(");
		while (!lexer.peekToken().equals(")")) {
			if (lexer.peekToken().equals("(")) {
				result.add(sExpression(lexer));
			} else {
				result.add(lexer.consumeToken());
			}

		}
		lexer.expect(")");
		return result;
	}

	/**
	 * Process 'dot' special form, for access to Java.
	 * @param args arguments
	 * @return result
	 * @see http://clojure.org/java_interop
	 */
	private static Object dot(final List<?> args, final Environment env) {
		if (args.size() < 2) {
			throw new EvalException(Errors.INVALID_NUMBER_OF_ARGUMENTS,
					2, -1, args.size()-1);
		}
		if (!(args.get(1) instanceof Symbol)) {
			throw new EvalException(Errors.INSTANCE_OR_CLASSNAME_EXPECTED,
					args.get(1));
		}

		final String methodName = ((Dot)args.get(0)).getMethodName();
		final String instanceOrClassName = ((Symbol)args.get(1)).toString();

		Object instance = null;
		Class<?> instanceClass = null;
		if (env.containsKey(instanceOrClassName)) {
			instance = env.get(instanceOrClassName);
			instanceClass = instance.getClass();
		} else {
			try {
				instanceClass = Class.forName(instanceOrClassName);
			} catch (ClassNotFoundException e) {
				throw new EvalException(
						Errors.INSTANCE_OR_CLASSNAME_NOT_FOUND,
						instanceOrClassName);
			}
		}

		List<Object> argInstances = new LinkedList<Object>();
		List<Class<?>> argClasses = new LinkedList<Class<?>>();
		for (int i=2; i<args.size(); ++i) {
			Object arg = args.get(i);
			final Object evaluatedArg = eval(arg, env);
			argInstances.add(evaluatedArg);
			argClasses.add(evaluatedArg.getClass());
		}

		Method method = null;
		try {
			method = instanceClass.getDeclaredMethod(methodName,
					argClasses.toArray(new Class<?>[] {}));

			// Sanity check for static calls
			if (instance == null && !Modifier.isStatic(method.getModifiers())) {
				throw new EvalException(Errors.NONSTATIC_CALL_IN_STATIC_CTX,
						method.toString());
			}

			return method.invoke(instance,
					argInstances.toArray(new Object[] {}));
		} catch (Exception e) {
			throw new EvalException(Errors.BAD_METHOD_INVOCATION,
					e.getClass().getName(), instanceOrClassName, methodName,
					e.getMessage());
		}
	}

	private static Object quote(final List<?> args) {
		if (args.size() != 2) {
			throw new EvalException(Errors.INVALID_NUMBER_OF_ARGUMENTS,
					1, 1, args.size()-1);
		}
		return args.get(1);
	}

	private static Object if_(final List<?> args, final Environment env) {
		if (args.size() < 3 || args.size() > 4) {
			throw new EvalException(Errors.INVALID_NUMBER_OF_ARGUMENTS,
					2, 3, args.size()-1);
		}
		final Object cond = eval(args.get(1), env);
		if (!(cond instanceof Boolean)) {
			throw new EvalException(Errors.BAD_IF_CONDITION,
					cond.getClass().getName());
		}
		if (((Boolean)cond).booleanValue()) {
			// conseq
			return eval(args.get(2), env);
		} else  {
			// alt
			if (args.size() == 4) {
				return eval(args.get(3), env);
			}
		}
		return null;
	}

	private static Object begin(final List<?> args, final Environment env) {
		if (args.size() < 2) {
			throw new EvalException(Errors.INVALID_NUMBER_OF_ARGUMENTS,
					1, -1, args.size()-1);
		}
		Object result = null;
		for (int i=1; i<args.size(); ++i) {
			result = eval(args.get(i), env);
		}
		return result;
	}

	private static Object set_(final List<?> args, final Environment env) {
		if (args.size() != 3) {
			throw new EvalException(Errors.INVALID_NUMBER_OF_ARGUMENTS,
					3, 3, args.size()-1);
		}
		if (!(args.get(1) instanceof Symbol)) {
			throw new EvalException(Errors.SYMBOL_EXPECTED);
		}
		final String newName = ((Symbol) args.get(1)).toString();
		if (!env.containsKey(newName)) {
			throw new EvalException(Errors.SYMBOL_MUST_BE_DEFINED,
					newName);
		}
		env.put(newName, eval(args.get(2), env));
		return null;
	}

	private static Object define(final List<?> args, final Environment env) {
		if (args.size() != 3) {
			throw new EvalException(Errors.INVALID_NUMBER_OF_ARGUMENTS,
					3, 3, args.size()-1);
		}
		if (!(args.get(1) instanceof Symbol)) {
			throw new EvalException(Errors.SYMBOL_EXPECTED);
		}
		final String name = ((Symbol) args.get(1)).toString();
		env.put(name, eval(args.get(2), env));
		return null;
	}

	/**
	 * Process 'lambda' special form
	 */
	private static Proc lambda(final List<?> args, final Environment env) {
		if (args.size() != 3) {
			throw new EvalException(Errors.INVALID_NUMBER_OF_ARGUMENTS,
					3, 3, args.size()-1);
		}
		if (!(args.get(1) instanceof List || args.get(1) instanceof Symbol)) {
			throw new EvalException(Errors.EXPECTED_LIST_OF_ARGUMENTS,
					args.get(1));
		}

		Proc result = null;
		final Object body = args.get(2);

		if (args.get(1) instanceof List) {
			// If formal args appear as list, bind each variable individually.

			final List<?> formalArgs = (List<?>) args.get(1);
			final List<String> formalArgNames = new LinkedList<String>();
			for (final Object o : formalArgs) {
				if (!(o instanceof Symbol)) {
					throw new EvalException(Errors.EXPECTED_LIST_OF_ARGUMENTS,
							o);
				}
				formalArgNames.add(((Symbol)o).toString());
			}
			result = new Proc(formalArgNames, env, body);

		} else if (args.get(1) instanceof Symbol) {

			 // If formal arg is a symbol, then all arguments are bound to a
			 // single variable when invoked.
			result = new Proc(Collections.singletonList(args.get(1).toString()),
					env, body, true);
		}

		return result;
	}

	private static Object proc(final List<?> list, final Environment env) {

		// Evaluate entire list.  Head element is proc object.  Tail is list
		// of actual arguments
		final List<Object> evaldList = new LinkedList<Object>();
		for (Object elem : list) {
			evaldList.add(eval(elem, env));
		}
		final int numOfActualArgs = evaldList.size() - 1;

		if (!(evaldList.get(0) instanceof Proc)) {
			throw new EvalException(Errors.PROC_EXPECTED);
		}

		final Proc proc = (Proc) evaldList.get(0);

		// Get formal arguments; do some sanity checks
		final List<String> formalArgs = proc.getFormalArguments();

		// Bind arguments
		final Environment procEnv = new Environment(proc.getEnvironment());
		if (proc.isBoundAllArgsAsList()) {
			// Turn all actual arguments into list, pass as sole parameter
			if (formalArgs.size() != 1) {
				throw new EvalException(Errors.INTERNAL_ERROR, "when binding " +
						"all arguments to proc as list, only one formal " +
						"argument allowed");
			}
			final List<Object> mergedActualArguments = new LinkedList<Object>();
			for (int i=1; i<evaldList.size(); ++i) {
				mergedActualArguments.add(evaldList.get(i));
			}
			procEnv.put(formalArgs.get(0), mergedActualArguments);
		} else {
			// Bind each argument to different formal argument
			if (formalArgs.size() != numOfActualArgs) {
				throw new EvalException(Errors.INVALID_NUMBER_OF_ARGUMENTS,
					formalArgs.size(), formalArgs.size(), numOfActualArgs);
			}

			for (int i=0; i<formalArgs.size(); ++i) {
				procEnv.put(formalArgs.get(i), evaldList.get(i+1));
			}
		}

		// Execute in nested environment
		return eval(proc.getBody(), procEnv);
	}

}
