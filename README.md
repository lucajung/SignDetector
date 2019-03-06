# SignDetector
SignDetector is a application that learns to understand your drawings.
For example it can detect characters, digits or even drawings.<br>
<img src="/images/SignDetector.png" width="300">

How does it work?
---
You add some training data by yourself, press train. Very easy.
Behind the GUI works my ([NeuralNetwork-Java-v2](https://github.com/lucajung/NeuralNetwork-Java-v2)) with as much input neurons as pixels are at the canvas.
The input layer is fully connected with the output layer. The output layer resize dynamically with the amount of different labels added to the training set.
To decode/ encode the output and to classify it, SignDetector uses one-hot encoding.
