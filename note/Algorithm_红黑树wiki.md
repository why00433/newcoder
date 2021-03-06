[TOC]

#### 参考文章

1. 《算法 第四版》第三章 查找
2. [重温数据结构：深入理解红黑树](http://blog.csdn.net/u011240877/article/details/53329023)
2. [ConcurrentHashMap的红黑树实现分析](http://www.jianshu.com/p/23b84ba9a498)

### 《算法 第四版》第三章 查找

无序数组查找->有序数组查找->二叉查找树->二叉平衡查找树->2-3查找树->红黑二叉查找树。

>2-3查找树能够做到：局部变换不会影响到树的全局有序性和平衡性。

红黑二叉查找树的基本思想：
1. 标准的二叉查找树（完全有2-结点构成）
2. 和 一些额外的信息（替换3-结点）

链接分为：
1. 红链接：将两个2-结点连接起来构成一个3-结点。
2. 黑链接：2-3查找树的普通链接。

>确切的说，将2-3查找树的3-结点表示为：由一条左斜的红链接相连的两个2-结点！（两个2-结点其中一个是另一个的左子结点。）





#### 红黑树

|[隐藏] 查 论 编|计算机科学中的树|
|:-----:|-----|
|二叉树|二叉查找树（BST） 笛卡尔树 MVP树 Top tree T树|
|自平衡二叉查找树    |AA树 AVL树 左倾红黑树 红黑树 替罪羊树 伸展树 树堆 加权平衡树|
|B树  |B+树 B*树 Bx树 UB树 2-3树 2-3-4树 (a,b)-树 Dancing tree H树|
|堆   |二叉堆 二项堆 斐波那契堆 左偏树 Pairing heap 斜堆 Van Emde Boas tree|
|Trie    |后缀树 基数树 三叉查找树 X-快速前缀树 Y-快速前缀树|
|二叉空间分区（BSP）树    |四叉树 八叉树 k-d树 隐式k-d树 VP树|
|非二叉树    |指数树 融合树 区间树 PQ树 Range tree SPQR树|
|空间数据分区树 |R树 R*树 R+树 X树 M树 线段树 希尔伯特R树 优先R树|
|其他树 |散列日历 散列树 Finger tree Order statistic tree Metric tree Cover tree BK树 Doubly chained tree iDistance Link-cut tree Fenwick tree Log-structured merge-tree 树状数组|

## 1.来自wiki的解释[红黑树](https://zh.wikipedia.org/wiki/%E7%BA%A2%E9%BB%91%E6%A0%91)

**红黑树**（英语：Red–black tree）是一种自平衡二叉查找树，是在计算机科学中用到的一种数据结构，典型的用途是**实现关联数组**。它是在1972年由鲁道夫·贝尔发明的，他称之为"对称二叉B树"，它现代的名字是在Leo J. Guibas和Robert Sedgewick于1978年写的一篇论文中获得的。它是复杂的，但它的操作有着良好的最坏情况运行时间，并且在实践中是高效的：它可以在`O(log n)`时间内做查找，插入和删除，这里的n是树中元素的数目。

### 1.1用途和好处

- 红黑树和AVL树一样都对 *插入时间、删除时间和查找时间*提供了最好可能的最坏情况担保。这不只是使它们在时间敏感的应用如实时应用（real time application）中有价值，而且使它们有在提供最坏情况担保的其他数据结构中作为建造板块的价值；例如，在计算几何中使用的很多数据结构都可以基于红黑树。
- 红黑树在 *函数式编程*中也特别有用，在这里它们是最常用的持久数据结构（persistent data structure）之一，它们用来构造 **关联数组和集合**，每次插入、删除之后它们能保持为以前的版本。除了O(log n)的时间之外，红黑树的持久版本对每次插入或删除需要O(log n)的空间。
- 红黑树是 **2-3-4树**的一种等同。换句话说，对于每个2-3-4树，都存在至少一个数据元素是同样次序的红黑树。在2-3-4树上的插入和删除操作也等同于在红黑树中颜色翻转和旋转。这使得2-3-4树成为理解红黑树背后的逻辑的重要工具，这也是很多介绍算法的教科书在红黑树之前介绍2-3-4树的原因，尽管2-3-4树在实践中不经常使用。
- 红黑树相对于AVL树来说， *牺牲了部分平衡性以换取插入/删除操作时少量的旋转操作*，整体来说性能要优于AVL树。


### 1.2性质

红黑树是每个节点都带有 *颜色属性*的二叉查找树，颜色为**红色或黑色**。在二叉查找树强制一般要求以外，对于任何有效的红黑树我们增加了如下的额外要求：

1. 节点是红色或黑色。
2. 根是黑色。
3. 所有叶子都是黑色（叶子是NIL节点）。
4. 每个红色节点必须有两个黑色的子节点。（从每个叶子到根的所有路径上不能有两个连续的红色节点。）
5. 从任一节点到其每个叶子的所有简单路径都包含相同数目的黑色节点。

>从根到叶子的最长的可能路径不多于最短的可能路径的两倍长。结果是这个树大致上是平衡的。
>性质4导致了路径不能有两个毗连的红色节点就足够了。  
>性质5所有最长的路径都有相同数目的黑色节点，这就表明了没有路径能多于任何其他路径的两倍长。
>使用"nil叶子"或"空（null）叶子"，它不包含数据而只充当树在此结束的指示。

### 1.3操作

因为每一个红黑树也是 **一个特化的二叉查找树**，  
>因此红黑树上的只读操作与普通二叉查找树上的只读操作相同。  
>然而，在红黑树上进行插入操作和删除操作会导致不再匹配红黑树的性质。  
>恢复红黑树的性质需要少量（O(log n)）的颜色变更（实际是非常快速的）和不超过三次树旋转（对于插入操作是两次）。  
>虽然插入和删除很复杂，但操作时间仍可以保持为O(log n)次。

#### 1.3.1 插入

我们首先以二叉查找树的方法增加节点并标记它为红色。（如果设为黑色，就会导致根到叶子的路径上有一条路上，多一个额外的黑节点，这个是很难调整的。但是设为红色节点后，可能会导致出现两个连续红色节点的冲突，那么可以通过颜色调换（color flips）和树旋转来调整。）下面要进行什么操作取决于其他临近节点的颜色。同人类的家族树中一样，我们将使用术语叔父节点来指一个节点的父节点的兄弟节点。注意：

- 性质1和性质3总是保持着。
- 性质4只在增加红色节点、重绘黑色节点为红色，或做旋转时受到威胁。
- 性质5只在增加黑色节点、重绘红色节点为黑色，或做旋转时受到威胁。

#### 1.3.2 删除

- 如果需要删除的节点有两个儿子，那么问题可以被转化成删除另一个只有一个儿子的节点的问题  
- 我们只需要讨论删除只有一个儿子的节点  
- 需要进一步讨论的是在要删除的节点和它的儿子二者都是黑色的时候


