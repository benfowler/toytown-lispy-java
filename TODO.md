TO DO
=====

Major Problems
--------------

* The dot (.) special form, borrowed from Clojure, is massively incomplete,
  and only resolves methods exactly matching the actual type of the arguments.
  Little effort is currently made to resolve methods with arguments which are
  type-compatible.
* Error reporting -- while vastly better than Norvig's cut-down Python 
  interpreter -- leaves a lot to be desired, especially in reporting locations
  of errors.
* Java interop needs finishing off.  Literals, property accesses, instance
  construction, etc, all need to work.
* It's slow! Profiling needs to be done; missing language features need to be
  added; misimplemented features need to be rewritten.
  

Enhancements
------------

* The list of builtins is very short, and needs padding out to be really useful
* List implementation should use something more conventional, so that
  performance characteristics of code can be better quantified
* Missing types: no integers, strings complex numbers, bignums
* Missing syntax: needs relational operators, cond, etc
* Could borrow more features from Clojure, e.g. literal list and map syntax...
* Needs to handle tail recursion elimination
* Needs to handle call/cc
* Needs hygienic macros

Future
------

* Compilation of procedures to bytecode would be desirable, for performance 
  reasons
