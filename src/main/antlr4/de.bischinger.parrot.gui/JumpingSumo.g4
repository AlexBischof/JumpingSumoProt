grammar JumpingSumo;

instructions:
    (instruction ';')+
    EOF;

instruction:
    instructionDelay
    | instructionForward
      | instructionBackward
        | instructionLeft
          | instructionRight
            | instructionJump
    ;

instructionDelay:
    'delay' paramMs=INTEGER;
instructionForward:
    'forward' paramMs=INTEGER;
instructionBackward:
    'backward' paramMs=INTEGER;
instructionLeft:
    'left' degrees=INTEGER;
instructionRight:
    'right' degrees=INTEGER;
instructionJump:
    'jump' type=JUMPTYPES;

JUMPTYPES: 'high' | 'long';
INTEGER:
    [0-9]+;
WS:
    [ \t\r\n\u000C]+ -> skip;
COMMENT:
    '/*' .*? '*/' -> skip;
LINE_COMMENT:
    '//' ~[\r\n]* -> skip;
STRING:
    '"' ( '\\"' | . )*? '"';
