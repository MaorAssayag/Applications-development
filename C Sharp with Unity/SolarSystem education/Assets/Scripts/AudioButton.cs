using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class AudioButton : MonoBehaviour {
    bool state = true;
    public AudioSource audioSource;
    public GameObject audioButton;
    public Sprite speaker;
    public Sprite mute;

    void Start () {
        onClick();
    }
	
	void Update () {
	}

    public void onClick()
    {
        state = !state;
        if (state)
        {
            audioSource.Play();
            audioButton.GetComponent<Image>().sprite = speaker;
        }
        else
        {
            audioSource.Pause();
            audioButton.GetComponent<Image>().sprite = mute;
        }
    }
}
