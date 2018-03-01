using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class mainCamera : MonoBehaviour {
    Transform mount;
    public Transform onStart;
    float speedFactor = 1f;
    float angleFactor = 10f;
    Sprite oldFact = null;
    public GameObject factPanel;

    // Use this for initialization
    void Start () {
        mount = onStart;
	}
	
	// Update is called once per frame
	void Update () {
        transform.position = Vector3.Lerp(transform.position,mount.position,speedFactor);
        transform.rotation = Quaternion.Slerp(transform.rotation, mount.rotation, angleFactor);
	}

    public void SetNewMount(Transform m)
    {
        mount = m;
    }
    public void SetNewFact(Sprite newFact)
    {
        if (oldFact != null)
        {
            factPanel.GetComponent<Image>().sprite = null;
        }
        if (newFact != null)
        {
            factPanel.GetComponent<Image>().sprite = newFact;
            oldFact = newFact;
        }
    }
}
