using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Diagnostics;
using System.Security.Principal;

namespace LightHotSpot
{
    public partial class Form1 : Form
    {
        bool connect = false;

        public Form1()
        {
            InitializeComponent();
        }

        public static bool IsAdmin()
        {
            WindowsIdentity id = WindowsIdentity.GetCurrent();
            WindowsPrincipal p = new WindowsPrincipal(id);
            return p.IsInRole(WindowsBuiltInRole.Administrator);
        }



        private void button1_Click(object sender, EventArgs e)
        {
            string ssid = textBox1.Text, key = textBox2.Text;
            if (!connect)
            {
                if (String.IsNullOrEmpty(textBox1.Text))
                {
                    richTextBox1.Clear();
                    richTextBox1.AppendText("SSID cannot be left blank !");
                }

                if (textBox2.Text == null || textBox2.Text == "")
                {
                    richTextBox1.AppendText("Key value cannot be left blank !");
                }

                else if (key.Length >= 6)
                {
                    Hotspot(ssid, key, true);
                    textBox1.Enabled = false;
                    textBox2.Enabled = false;
                    button1.Text = "Stop";
                    button3.Text = "On";
                    button3.BackColor = Color.Green;
                    connect = true;
                }
                else
                {
                    richTextBox1.Clear();
                    richTextBox1.AppendText("Inavlid Password! at least 6 char's !");
                }
            }
            
            else
            {
                Hotspot(null, null, false);
                textBox1.Enabled = true;
                textBox2.Enabled = true;
                button1.Text = "Start";
                button3.Text = "Off";
                button3.BackColor = Color.IndianRed;
                connect = false;
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            this.WindowState = FormWindowState.Minimized;
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            if (!IsAdmin())
            {
                RestartElevated();
            }
        }

        public void RestartElevated()
        {
            ProcessStartInfo startInfo = new ProcessStartInfo();
            startInfo.UseShellExecute = true;
            startInfo.CreateNoWindow = true;
            startInfo.WorkingDirectory = Environment.CurrentDirectory;
            startInfo.FileName = System.Windows.Forms.Application.ExecutablePath;
            startInfo.Verb = "runas";
            try {  Process p = Process.Start(startInfo); }
            catch {   }
            System.Windows.Forms.Application.Exit();
        }

        private void Hotspot(string ssid, string key, bool status)
        {
            ProcessStartInfo processStartInfo = new ProcessStartInfo("cmd.exe");
            processStartInfo.RedirectStandardInput = true;
            processStartInfo.RedirectStandardOutput = true;
            processStartInfo.CreateNoWindow = true;
            processStartInfo.UseShellExecute = false;
            Process process = Process.Start(processStartInfo);

            if (process != null)
            {
                if (status)
                {
                    process.StandardInput.WriteLine("netsh wlan set hostednetwork mode=allow ssid=" + ssid + " key=" + key);
                    process.StandardInput.WriteLine("netsh wlan start hostednetwork");
                    process.StandardInput.Close();
                    richTextBox1.Clear();
                    richTextBox1.AppendText("Hotspot started !");
                    
                }
                else
                {
                    process.StandardInput.WriteLine("netsh wlan stop hostednetwork");
                    process.StandardInput.Close();
                    richTextBox1.Clear();
                    richTextBox1.AppendText("Hotspot stop !");
                }
            }
        }

        // mouse can move the form
        int mouseX = 0, mouseY = 0;
        bool mouseDown;
        private void pictureBox1_MouseMove(object sender, MouseEventArgs e)
        {
            if (mouseDown)
            {
                mouseX = MousePosition.X - 300;
                mouseY = MousePosition.Y - 4;
                this.SetDesktopLocation(mouseX, mouseY);
            }
        }

        private void pictureBox1_MouseUp(object sender, MouseEventArgs e)
        { mouseDown = false; }

        private void pictureBox1_MouseDown(object sender, MouseEventArgs e)
        { mouseDown = true; }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }

        private void pictureBox2_Click(object sender, EventArgs e)
        {
            richTextBox1.Clear();
            richTextBox1.AppendText("Smile More :)");
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            Hotspot(null, null, false);
            Application.Exit();
        }
    }
}
