(begin
  (define filter (lambda (predicate argument)
    (if (null? argument)
      (quote ())
      (if (predicate (car argument))
        (cons (car argument) (filter predicate (cdr argument)))
        (filter predicate (cdr argument))))))

  (define partition (lambda (op pivot ls)
    (filter (lambda (e) (op e pivot)) ls)))

  (define quicksort (lambda (ls)
    (if (null? ls)
        (quote ())
        (begin (define pivot (car ls))
               (define rest (cdr ls))
               (define left (partition < pivot rest))
               (define right (partition >= pivot rest))
               (append (quicksort left) (list pivot) (quicksort right))))))

  (define qsort quicksort)

  (display (qsort (quote ())))
  (display (qsort (quote (0))))
  (display (qsort (quote (1 2 3 4 5))))
  (display (qsort (quote (5 4 3 2 1))))
  (display (qsort (quote (0 -1 1 -2 2 -3 3 -4 4 -5 5))))
  (display (qsort (quote (-99 0 1024 202 -1 77 18 -34 2048 19 1 9 7 3 2 -3 191))))

  (equal? (qsort (quote (5 4 3 2 1))) (quote (1 2 3 4 5)))
)
