import React, {useEffect, useState} from "react";
import {
  Dialog,
  AppBar,
  Toolbar,
  IconButton,
  Typography,
  Slide,
  DialogContent,
  DialogActions,
  Button
} from "@material-ui/core";
import {makeStyles} from '@material-ui/core/styles';
import CloseIcon from '@material-ui/icons/Close';
import FilesDropzone from "../../../../components/FilesDropzone";
import {fileService, searchService} from "../../../../services";
import {useKeycloak} from "@react-keycloak/web";
import ItemProgressDialog from "../../../Search/Index/AddDocumentsDialog/ItemProgressDialog";

const useStyles = makeStyles(theme => ({
  appBar: {
    position: 'relative',
  },
  title: {
    marginLeft: theme.spacing(2),
    flex: 1,
    color: theme.palette.common.white
  },
  backButton: {
    marginRight: theme.spacing(1),
  },
}));

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="down" ref={ref} {...props} />;
});

function SubmitFilesDialog({open, onClose, onCancel, onSubmit}) {

  const classes = useStyles();
  const {keycloak} = useKeycloak();
  const [currentItem, setCurrentItem] = useState(null);
  const [completed, setCompleted] = useState(0);
  const [files, setFiles] = useState([]);
  const [uploadedFiles, setUploadedFiles] = useState([]);
  const [uploading, setUploading] = useState(false);

  useEffect(() => {
    if (!open) {
      setFiles([]);
      setUploadedFiles([]);
      setCurrentItem(null);
      setCompleted(0);
      setUploading(false);
    }
  }, [open]);

  useEffect(() => {
    if (uploading) {
      uploadFile(files, 0, []);
    }
  }, [uploading]);

  useEffect(() => {
    if (uploadedFiles.length > 0 && uploadedFiles.length === files.length) {
      onSubmit(uploadedFiles);
    }
  }, [uploadedFiles]);

  const handleOnFilesChanged = (files) => {
    setFiles(files);
  };

  const handleUploadCancel = () => {
    setUploading(false);
  };


  const uploadFile = (files, i, uploaded) => {
    if (!uploading)
      return;

    const file = files[i];
    const totalSize = files
      .map(f => f.size)
      .reduce((s1, s2) => s1 + s2, 0);
    let processedSize = 0.0;
    for (let j = 0; j <= i; j++) {
      processedSize += files[j].size;
    }
    setCompleted((processedSize / totalSize) * 100);
    fileService.upload(file, keycloak.idToken)
      .then((res) => {
        i = i + 1;
        if (i >= files.length) {
          setUploading(false);
          setUploadedFiles([...uploaded, res]);
        } else {
          uploadFile(files, i, [...uploaded, res]);
        }
      });
  };

  const handleSubmit = () => {
    if (files.length > 0) {
      setCompleted(0);
      setUploading(true);
    }
  };

  return (
    <>
      <Dialog open={open} TransitionComponent={Transition} maxWidth="lg" fullWidth>
        <AppBar className={classes.appBar}>
          <Toolbar>
            <IconButton edge="start" color="inherit" onClick={onClose} aria-label="close">
              <CloseIcon/>
            </IconButton>
            <Typography variant="h4" className={classes.title}>
              Submit Files
            </Typography>
          </Toolbar>
        </AppBar>
        <DialogContent>
          <FilesDropzone files={files} onFilesChanged={handleOnFilesChanged}/>
        </DialogContent>
        <DialogActions>
          <DialogActions>
            <Button
              variant="text"
              onClick={onCancel}>Cancel</Button>
          </DialogActions>
          <Button variant="contained"
                  color="primary"
                  onClick={handleSubmit}>
            Submit
          </Button>
        </DialogActions>
      </Dialog>
      <ItemProgressDialog open={uploading}
                          currentItem={currentItem}
                          completed={completed}
                          onCancel={handleUploadCancel}/>
    </>
  )
}

export default SubmitFilesDialog;
