# ICS 202 Lab Project - Terminal Simulator

## Team Members:
1. Abdelrahman Diab, ID: 202444200
2. Qais Abu Adas, ID: 202465300

## Data Structures Used

| Structure            | Location                | Purpose                         |
|----------------------|-------------------------|---------------------------------|
| **HashMap**          | `Directory.children`    | O(1) file/directory lookup      |
| **LabStack**         | `FileSystem.pwd()`      | Build path from current to root |
| **Tree (Composite)** | `Node → File/Directory` | Hierarchical file system        |
| **KMP Array**        | `KMP.java`              | Pattern matching for grep       |

## Sample Output

```
/$ mkdir -p home/test/Documents
/$ cd home/test
/home/test$ touch report.txt 1024
/home/test$ echo "hello world" > notes.txt
/home/test$ ls
Documents/ notes.txt (11B) report.txt (1024B)
/home/test$ cd /
/$ tree
. 
└── home/
    └── test/
        ├── Documents/
        ├── notes.txt (11B)
        └── report.txt (1024B)
/$ du
Total size: 1035B
/$ grep "world" home/test/notes. txt
Pattern "world" found in notes.txt. 
/$ exit
```

## Commands

| Command | Example                          |
|---------|----------------------------------|
| `mkdir` | `mkdir docs` or `mkdir -p a/b/c` |
| `touch` | `touch file. txt 100`            |
| `echo`  | `echo "text" > file. txt`        |
| `ls`    | `ls`                             |
| `cd`    | `cd ..` or `cd /home`            |
| `pwd`   | `pwd`                            |
| `rm`    | `rm file` or `rm -r folder`      |
| `tree`  | `tree`                           |
| `du`    | `du`                             |
| `grep`  | `grep "pattern" file.txt`        |
| `exit`  | `exit`                           |