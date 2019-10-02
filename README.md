# LabelingPlatform 数据标注平台

## 业务流
![](imgs/process.png)

### 无标签数据：
  txt或csv文件，每行一条数据
### 带标签数据：
  json文件，格式：[{"item": "xxx1", "defaultTag": "yyy1"}, {"item": "xxx2", "defaultTag": "yyy2"}, ...]

## 数据流
![](imgs/dataflow.png)
*创建任务的专家可以随时导出数据，训练模型，更新数据标签*

## ER图（略去属性）
![](imgs/er.png)
* 一个人标多条数据，一条数据被多个人标
* 一个团队有多个标注成员，一个标注员只属于某个团队
* 一个团队负责一个任务，一个任务可由多个团队完成
* 每个任务指定标注数据集，每条数据归属于某个具体任务

## 数据库设计小结
> 冗余是解决效率问题的最有效手段之一！

> 不要将相关联的数据(表)存放在不同的数据库（如mysql和mongodb）中！

> 尽量减少数据库的访问次数！
