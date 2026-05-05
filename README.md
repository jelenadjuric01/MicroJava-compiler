# MicroJava Compiler

A compiler for **MicroJava (MJ)** — a small, Java-like teaching language — built as a coursework project for *Compiler Construction (PP1)* at the School of Electrical Engineering, University of Belgrade.

The compiler reads MJ source code and produces MicroJava bytecode (`.obj`) that runs on the MicroJava Virtual Machine, a simple stack-based interpreter shipped as `lib/mj-runtime-1.1.jar`.

## What MicroJava Is

MicroJava is a stripped-down dialect of Java designed for teaching compiler construction. Like Java, it is statically typed and compiled to bytecode for a dedicated VM, but its grammar is intentionally smaller so every phase of compilation — lexing, parsing, semantic analysis, code generation — fits inside a one-semester course.

## Language Features

Implemented in the lexer and grammar:

- **Primitive types**: `int`, `char`, `bool`
- **Constants and variables**: `const`, scalar and one-dimensional array declarations
- **Control flow**: `if` / `else`, `for` (with `break` and `continue`)
- **Methods**: `void` and value-returning methods, with formal parameters and local variables
- **Classes**: `class` with `extends` (single inheritance)
- **Namespaces**: `namespace` definitions and `using` directives
- **I/O**: `print` (with optional width specifier) and `read`
- **Operators**: arithmetic (`+ - * / % ^`), relational (`== != < <= > >=`), logical (`&& ||`), assignment (`=`), increment/decrement (`++ --`), array indexing, member access
- **Literals**: integer, character (`'.'`), boolean (`true` / `false`)
- **Comments**: `// …` to end of line

## Compiler Pipeline

```
.mj source
    │
    ▼
┌──────────────┐   JFlex (spec/mjlexer.flex)
│   Lexer      │   →  src/.../Yylex.java
└──────────────┘
    │ token stream
    ▼
┌──────────────┐   CUP (spec/mjparser.cup, mjparser_astbuild.cup)
│   Parser     │   →  src/.../MJParser.java
│   + AST      │   →  src/.../ast/*
└──────────────┘
    │ AST
    ▼
┌──────────────┐   Visitor pattern over AST nodes
│  Semantic    │   src/.../SemanticAnalyzer.java
│  Analysis    │   • symbol table (rs.etf.pp1.symboltable)
│              │   • type checking, scope resolution
│              │   • main method / namespace validation
└──────────────┘
    │ annotated AST
    ▼
┌──────────────┐   Visitor pattern, emits MJ bytecode
│   Code       │   src/.../CodeGenerator.java
│   Generation │   • backpatching for if/for/&&/||
│              │   • break / continue patch lists
└──────────────┘
    │
    ▼
program.obj  ──►  MicroJava VM (rs.etf.pp1.mj.runtime.Run)
```

`CounterVisitor` is a small helper visitor used to count formal/local variables before code emission.

## Project Layout

```
.
├── build.xml                # Ant build (lexer/parser gen, compile, run)
├── spec/
│   ├── mjlexer.flex         # JFlex lexer specification
│   ├── mjparser.cup         # CUP parser specification
│   └── mjparser_astbuild.cup
└── src/rs/ac/bg/etf/pp1/
    ├── Yylex.java           # generated lexer
    ├── MJParser.java        # generated parser
    ├── sym.java             # generated terminal symbols
    ├── ast/                 # generated AST nodes + Visitor / VisitorAdaptor
    ├── SemanticAnalyzer.java
    ├── CodeGenerator.java
    ├── CounterVisitor.java
    └── util/Log4JUtils.java
```

External jars under `lib/` (not tracked here): `JFlex.jar`, `cup_v10k.jar`, `log4j-1.2.17.jar`, `symboltable-1-1.jar`, `mj-runtime-1.1.jar`.

## Building

The project uses Apache Ant. Targets are defined in `build.xml`:

| Target       | What it does                                                    |
|--------------|-----------------------------------------------------------------|
| `delete`     | Removes generated lexer/parser/AST files (keeps hand-written).  |
| `lexerGen`   | Runs JFlex on `spec/mjlexer.flex` to generate `Yylex.java`.     |
| `parserGen`  | Runs CUP on `spec/mjparser.cup` to generate parser + AST.       |
| `repackage`  | Rewrites generated AST package to `rs.ac.bg.etf.pp1.ast`.       |
| `compile`    | Compiles all Java sources against the libs in `lib/`.           |
| `disasm`     | Disassembles `test/program.obj` via the MJ runtime.             |
| `runObj`     | Disassembles and runs `test/program.obj` on the MJ VM in debug. |

Typical workflow:

```bash
ant compile            # regenerates lexer + parser, then compiles everything
ant runObj             # runs the produced program.obj on the MJ VM
```

`ant compile` chains `delete → parserGen → repackage → compile`, so running it from a clean tree regenerates the parser and AST automatically. Run `ant lexerGen` separately when only the lexer specification has changed.

## Running a MicroJava Program

1. Place a compiled MJ object file at `test/program.obj`.
2. `ant runObj` disassembles it and then executes it on the MicroJava VM with `-debug`.

## Status

This is a coursework implementation — useful for studying the front-to-back of a small compiler (lexer, LALR parser with AST construction, symbol-table-driven semantic analysis, and stack-machine code generation with backpatching), not a production toolchain.

## License

Educational use. The MicroJava language, runtime, and CUP/JFlex specifications follow the conventions of the PP1 course at the University of Belgrade, School of Electrical Engineering.
