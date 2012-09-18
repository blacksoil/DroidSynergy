#include <stdio.h>
#include <stdlib.h>
#include <linux/input.h>
#include <fcntl.h>
#include <errno.h>
#include <string.h>

#include "uinput.h"

void Print(char *msg){
  printf("%s\n", msg);
}

int Err(char *msg){
  Print(msg);
  //exit(EXIT_FAILURE);
  return 1;
}

void SleepMs(int ms){
  int us = ms * 1000;
  if(usleep(us)){
    Print("Sleep fails");
  }
}

// Settings

// Delay between mouse button down and up
// in ms
const int MOUSE_CLICK_DELAY = 60;


const char *UINPUT_PATH =
  "/dev/uinput";

const char *DEV_NAME =
  "synergy-input";

int _fd;

int WriteToFd(void *ptr, int size){
  int ret;
  if ((ret = write(_fd, ptr, size)) != size){
    Print("Write isn't completed!");
  }
  return ret;
}

// Initializes what's needed
int InitUinput(){
  struct uinput_user_dev dev;

  _fd = open(UINPUT_PATH, O_NDELAY | O_WRONLY);
  if(_fd == -1){
    Err(strerror(errno));
  }

  if (ioctl(_fd, UI_SET_EVBIT, EV_REL) == -1 ||
      ioctl(_fd, UI_SET_RELBIT, REL_X) == -1 ||
      ioctl(_fd, UI_SET_RELBIT, REL_Y) == -1 ||
      ioctl(_fd, UI_SET_EVBIT, EV_SYN) == -1 ||
      ioctl(_fd, UI_SET_EVBIT, EV_KEY) == -1 ||
      ioctl(_fd, UI_SET_KEYBIT, BTN_MOUSE) == -1)
    {
      Print("IOCTL failed:");
      return Err(strerror(errno));
    }

  // Write the device name
  memset(&dev, 0, sizeof(dev));
  strncpy(dev.name, DEV_NAME, strlen(DEV_NAME));
  WriteToFd((void*)&dev, sizeof(dev));

  if (ioctl(_fd, UI_DEV_CREATE) == -1){
    Print("UI_DEV_CREATE failed:");
    return Err(strerror(errno));
  }

  Print("Input device has been initialized");
  return 0;
}

// Write the input_event to
// _fd
// Return 0 on failed, 1 on success
int WriteToInput(uint16_t type,
                 uint16_t code,
                 int32_t value) {
  struct input_event event;
  memset(&event , 0, sizeof(event));
  event.type = type;
  event.code = code;
  event.value = value;

  return WriteToFd(&event, sizeof(event));

}

// Like WriteToInput but also write sync
int WriteToInputSyn(uint16_t type,
                    uint16_t code,
                    int32_t value) {
  if(!WriteToInput(type, code, value)){
    return 0;
  }

  return WriteToInput(EV_SYN, SYN_REPORT, 0);
}

int MouseX(int x){
  return WriteToInput(EV_REL, REL_X, x);
}

int MouseXSyn(int x){
  return WriteToInputSyn(EV_REL, REL_X, x);
}


int MouseY(int y){
  return WriteToInput(EV_REL, REL_Y, y);
}

int MouseYSyn(int y){
  return WriteToInputSyn(EV_REL, REL_Y, y);
}

int MouseRightClickDelay(int delayMs){
  // DOWN
  WriteToInputSyn(EV_KEY, BTN_RIGHT, 0);
  SleepMs(delayMs);
  // UP;
  WriteToInputSyn(EV_KEY, BTN_RIGHT, 1);
  SleepMs(delayMs);
}

int MouseLeftClickDelay(int delayMs){
  // DOWN
  WriteToInputSyn(EV_KEY, BTN_LEFT, 0);
  SleepMs(delayMs);
  // UP;
  WriteToInputSyn(EV_KEY, BTN_LEFT, 1);
  SleepMs(delayMs);
}

int MouseRightClick(){
  MouseRightClickDelay(MOUSE_CLICK_DELAY);
}

int MouseLeftClick(){
  MouseLeftClickDelay(MOUSE_CLICK_DELAY);
}

void Test(){
  int i;
  for(i = 0 ; i < 200 ; i++){
    MouseXSyn(3);
    MouseYSyn(-3);
    SleepMs(10);
  }
}

/*
int main(int arg, char **argv){
  InitUinput();
  //Test();
  //MouseLeftClick();
  return 0;
}
*/
