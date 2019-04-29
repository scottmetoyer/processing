import processing.serial.*;
import themidibus.*;
import javax.sound.midi.MidiMessage;

int shapeCount = 50;
int layerCount = 3;
int triggerCount = 0;
int triggerThreshold = 16;
Shape[][] layers = new Shape[layerCount][shapeCount];

Serial serialPort;
String serialInput;
int imageCount = 3;
int currentImageIndex = 0;

MidiBus midiBus;
int midiDevice = 1; // Set to correct MIDI input as displayed in the console (IAC Bus 1)

void setup() {
  //fullScreen(P2D);
  size(800, 600, P2D);
  String[] images = { "b1.jpg", "b2.jpg", "b3.jpg" };

  for (int i = 0; i < shapeCount; i++) {
    layers[0][i] = new Shard(images, imageCount);
    layers[0][i].setOpacity((int)random(128, 255));
    layers[0][i].setSize(1.3);
  }

  for (int i = 0; i < shapeCount; i++) {
    layers[1][i] = new Shard(images, imageCount);
    layers[1][i].setOpacity((int)random(128, 255));
  }

  for (int i = 0; i < shapeCount; i++) {
    layers[2][i] = new Shard(images, imageCount);
    layers[2][i].setOpacity((int)random(128, 255));
    layers[2][i].setSize(0.7);
  }

  //smooth();
  frameRate(60);

  // Open the MIDI port
  MidiBus.list();
  midiBus = new MidiBus(this, midiDevice, 1);

  // Open up the serial port
  // String portName = Serial.list()[1];
  // serialPort = new Serial(this, portName, 9600);
}

void midiMessage(MidiMessage message, long timestamp, String bus_name) {
  int note = (int)(message.getMessage()[1] & 0xFF) ;
  int vel = (int)(message.getMessage()[2] & 0xFF);

  println("Bus " + bus_name + ": Note "+ note + ", vel " + vel);
  String trigger;

  switch(note) {
    case 60:
    trigger = "trigger_1\n";
    break;

    case 61:
    trigger = "trigger_2\n";
    break;

    default:
    trigger = "trigger_3\n";
    break;
  }

  if (vel > 0 ) {
    processInput(trigger);
  }
}

int randomExcept(int top, int exclude) {
  int number;

  do {
    number = (int)random(top);
  } while (number == exclude);

  return number;
}

void pulseRandomShapes() {
  int layer = (int)random(layerCount);
  int shapes = (int)random(shapeCount);

  for (int i = 0; i < shapes; i++) {
     layers[layer][i].triggerPulse();
  }
}

void pulseAllShapes(int layer) {
  for (int i = 0; i < shapeCount; i++) {
     layers[layer][i].triggerPulse();
  }
}

void moveRandomLayer() {
  int layer = (int)random(layerCount);
  for (int i = 0; i < shapeCount; i++) {
    layers[layer][i].triggerMove();
  }
}

void moveAllLayers() {
  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shapeCount; i++) {
      layers[j][i].triggerMove();
    }
  }
}

void setRandomImage() {
  currentImageIndex = randomExcept(imageCount, currentImageIndex);

  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shapeCount; i++) {
      layers[j][i].setImageIndex(currentImageIndex);
    }
  }
}

void resetAll() {
  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shapeCount; i++) {
      layers[j][i].reset();
    }
  }
}

void keyPressed() {
  // Pulse random number of shards on random layer
  if (key == 'z') {
    pulseRandomShapes();
  }

  // Pulse all shards layer 1
  if (key == 'x') {
    pulseAllShapes(0);
  }

  // Pulse all shards layer 2
  if (key == 'c') {
    pulseAllShapes(1);
  }

  // Pulse all shards layer 3
  if (key == 'v') {
    pulseAllShapes(2);
  }

  // Rotate random layer
  if (key == 'a') {
    moveRandomLayer();
  }

  // Rotate all layers
  if (key == 's') {
    moveAllLayers();
  }

  // Toggle layer visible
  if (key == 'd') {
    resetAll();
  }

  // Reset rotation?
  if (key == 'f') {
    setRandomImage();
  }

  /*
  if (key == 'r') {
     for (int i = 0; i < shardCount; i++) {
      layer3[i].setVisible(!(layer3[i].getVisible()));
    }
  }*/
}

void processInput(String input) {
  int randomChoice;

  switch(input) {
    case "trigger_1\n":
    if (triggerCount++ >= triggerThreshold) {
      randomChoice = (int)random(2);

      if (randomChoice == 0) {
        triggerCount = 0;
        resetAll();
      }
    } else {
      randomChoice = (int)random(2);

      if (randomChoice == 0) {
        moveRandomLayer();
      } else {
        moveAllLayers();
      }
    }
    break;

    case "trigger_2\n":
    pulseRandomShapes();

    randomChoice = (int)random(6);
    if (randomChoice == 0) {
      setRandomImage();
    }
    break;

    case "trigger_3\n":
    randomChoice = (int)random(6);

    if (randomChoice == 5) {
      pulseAllShapes(0);
      pulseAllShapes(1);
      pulseAllShapes(2);
    } else if (randomChoice == 4) {
      pulseAllShapes(1);
      pulseAllShapes(2);
    } else if (randomChoice == 3) {
      pulseAllShapes(1);
      pulseAllShapes(2);
    } else {
      pulseAllShapes(randomChoice);
    }
    break;
  }
}

void processSerialInput() {
  if (serialPort.available() > 0) {
    serialInput = serialPort.readStringUntil('\n');
    print(serialInput);
    processInput(serialInput);
  }
}

void draw(){
  // Check for serial data - unccomment for serial input, comment for MIDI input
  // processSerialInput();

  background(0);

  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shapeCount  ; i++) {
      layers[j][i].update();
      layers[j][i].display();
    }
  }
}