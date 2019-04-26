java中四种修饰符的限制范围:
| 修饰符 | 本类访问 | 包访问 | 不同包子类访问 | 不同包的非子类访问 |
| :------: | :------: | :------: | :------: | :------: |
| public|y|y|y|y|
| default|y|y|y||
| protected|y|y|||
| private|y||||
public：可以被任意类访问
default：包内及子类访问
producted：包内部访问
private：本类自己访问