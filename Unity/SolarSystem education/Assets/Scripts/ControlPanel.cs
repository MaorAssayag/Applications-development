using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ControlPanel : MonoBehaviour {
    private bool state = false;
    public GameObject Panel;
    public GameObject menuButton;
    public Sprite right;
    public Sprite left;

    void Start () {
        OnClick();
    }
	
	void Update () {
	}

    public void OnClick()
    {
        state = !state;
        Panel.SetActive(state);
        if (state)
        {
            menuButton.GetComponent<Image>().sprite = left;
        }
        else
        {
            menuButton.GetComponent<Image>().sprite = right;
        }
        
    }
}