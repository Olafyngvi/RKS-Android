using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Massage.ViewModels
{
    public class RezervacijeVM
    {
        public int Id { get; set; }
        public int Dan { get; set; }
        public int Mjesec { get; set; }
        public int Godina { get; set; }
        public int TerminId { get; set; }
        public string Termin { get; set; }
    }
}
